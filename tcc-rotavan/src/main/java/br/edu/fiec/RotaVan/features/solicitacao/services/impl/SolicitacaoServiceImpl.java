package br.edu.fiec.RotaVan.features.solicitacao.services.impl;

import br.edu.fiec.RotaVan.features.contratos.services.ContratoService;
import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.repositories.PontoRepository;
import br.edu.fiec.RotaVan.features.rotas.repositories.RotaRepository;
import br.edu.fiec.RotaVan.features.solicitacao.dto.DecisaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.dto.SolicitacaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;
import br.edu.fiec.RotaVan.features.solicitacao.repositories.SolicitacaoRepository;
import br.edu.fiec.RotaVan.features.solicitacao.services.SolicitacaoService;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.repositories.CriancaRepository;
import br.edu.fiec.RotaVan.features.user.repositories.EscolasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.shared.dto.ResultadoOtimizadoDTO;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private static final Logger log = LoggerFactory.getLogger(SolicitacaoServiceImpl.class);

    private final SolicitacaoRepository solicitacaoRepository;
    private final ResponsaveisRepository responsavelRepository;
    private final MotoristasRepository motoristaRepository;
    private final CriancaRepository criancaRepository;
    private final EscolasRepository escolaRepository;
    private final ContratoService contratoService;
    private final RotaRepository rotaRepository;
    private final PontoRepository pontoRepository;
    private final GoogleMapsService googleMapsService;

    public SolicitacaoServiceImpl(
            SolicitacaoRepository solicitacaoRepository,
            ResponsaveisRepository responsavelRepository,
            MotoristasRepository motoristaRepository,
            CriancaRepository criancaRepository,
            EscolasRepository escolaRepository,
            ContratoService contratoService,
            RotaRepository rotaRepository,
            PontoRepository pontoRepository,
            GoogleMapsService googleMapsService) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.responsavelRepository = responsavelRepository;
        this.motoristaRepository = motoristaRepository;
        this.criancaRepository = criancaRepository;
        this.escolaRepository = escolaRepository;
        this.contratoService = contratoService;
        this.rotaRepository = rotaRepository;
        this.pontoRepository = pontoRepository;
        this.googleMapsService = googleMapsService;
    }

    @Override
    public List<Solicitacao> listarTodas() {
        return solicitacaoRepository.findAll();
    }

    @Override
    public Optional<Solicitacao> findById(UUID id) {
        return solicitacaoRepository.findById(id);
    }

    @Override
    @Transactional
    public Solicitacao criarSolicitacaoEGerarRotas(SolicitacaoRequestDTO request) {
        Responsaveis responsavel = responsavelRepository.findById(request.getResponsavelId())
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado"));
        Motoristas motorista = motoristaRepository.findById(request.getMotoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado"));
        Crianca crianca = criancaRepository.findById(request.getCriancaId())
                .orElseThrow(() -> new RuntimeException("Criança não encontrada"));
        Escolas escola = escolaRepository.findById(request.getEscolaId())
                .orElseThrow(() -> new RuntimeException("Escola não encontrada"));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setResponsavel(responsavel);
        solicitacao.setMotorista(motorista);
        solicitacao.setCrianca(crianca);
        solicitacao.setEscola(escola);
        solicitacao.setStatus("PENDENTE");

        solicitacao = solicitacaoRepository.save(solicitacao);

        try {
            // Tenta obter o endereço do motorista, usa padrão se for nulo
            String origem = (motorista.getUser().getEndereco() != null) ? motorista.getUser().getEndereco() : "Indaiatuba, SP";
            String destino = escola.getEndereco();
            List<String> paradas = List.of(crianca.getEndereco());

            if (origem != null && destino != null) {
                ResultadoOtimizadoDTO resultadoOtimizado = googleMapsService.otimizarRota(origem, destino, paradas);

                if (resultadoOtimizado != null) {
                    Rota rotaSugerida = new Rota();
                    rotaSugerida.setNome("Rota Sugerida - " + crianca.getNome());

                    // --- CORREÇÃO AQUI (Nomes dos métodos setters) ---
                    rotaSugerida.setDistancia(resultadoOtimizado.getDistanciaKm());
                    rotaSugerida.setTempoEstimado(resultadoOtimizado.getTempoMin());
                    // -------------------------------------------------

                    rotaSugerida.setTipo("SUGERIDA");
                    rotaSugerida.setSolicitacao(solicitacao);

                    rotaSugerida = rotaRepository.save(rotaSugerida);

                    for (Ponto pontoDTO : resultadoOtimizado.getPontosOrdenados()) {
                        Ponto ponto = new Ponto();
                        ponto.setEndereco(pontoDTO.getEndereco());
                        ponto.setLatitude(pontoDTO.getLatitude());
                        ponto.setLongitude(pontoDTO.getLongitude());
                        ponto.setOrdem(pontoDTO.getOrdem());
                        ponto.setNome(pontoDTO.getNome());
                        ponto.setRota(rotaSugerida);

                        pontoRepository.save(ponto);
                    }
                    log.info("Rota sugerida criada com sucesso para a solicitação {}", solicitacao.getId());
                }
            }
        } catch (Exception e) {
            log.error("Erro ao gerar rota sugerida: " + e.getMessage());
        }

        return solicitacao;
    }

    @Override
    @Transactional
    public Solicitacao decidirSolicitacao(UUID solicitacaoId, DecisaoRequestDTO decisao) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        if (!"PENDENTE".equalsIgnoreCase(solicitacao.getStatus())) {
            throw new IllegalStateException("Esta solicitação já foi respondida.");
        }

        String novoStatus = decisao.isAceito() ? "ACEITA" : "RECUSADA";
        solicitacao.setStatus(novoStatus);

        if (decisao.isAceito()) {
            contratoService.criarContrato(
                    solicitacao.getResponsavel(),
                    solicitacao.getMotorista(),
                    solicitacao.getCrianca()
            );
        }

        return solicitacaoRepository.save(solicitacao);
    }

    public List<Solicitacao> listarPorStatus(String status) {
        return solicitacaoRepository.findByStatus(status);
    }

    public List<Solicitacao> listarPorMotorista(UUID motoristaId) {
        return solicitacaoRepository.findByMotoristaId(motoristaId);
    }

    public List<Solicitacao> listarPorResponsavel(UUID responsavelId) {
        return solicitacaoRepository.findByResponsavelId(responsavelId);
    }

    public boolean deleteById(UUID id) {
        if (solicitacaoRepository.existsById(id)) {
            solicitacaoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}