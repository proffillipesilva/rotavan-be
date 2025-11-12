package br.edu.fiec.RotaVan.features.solicitacao.services;

import br.edu.fiec.RotaVan.features.contratos.models.Contrato;
import br.edu.fiec.RotaVan.features.contratos.repositories.ContratoRepository;
import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.repositories.PontoRepository;
import br.edu.fiec.RotaVan.features.rotas.repositories.RotaRepository;
import br.edu.fiec.RotaVan.shared.dto.ResultadoOtimizadoDTO;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService;
import br.edu.fiec.RotaVan.features.solicitacao.dto.DecisaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.dto.SolicitacaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;
import br.edu.fiec.RotaVan.features.solicitacao.repositories.SolicitacaoRepository;
import br.edu.fiec.RotaVan.features.user.models.*;
import br.edu.fiec.RotaVan.features.user.repositories.CriancaRepository;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor // Injeta todos os Repositories e o novo Service
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final CriancaRepository criancaRepository;
    private final MotoristasRepository motoristasRepository;
    private final ResponsaveisRepository responsaveisRepository;
    private final RotaRepository rotaRepository;
    private final PontoRepository pontoRepository;
    private final ContratoRepository contratoRepository;
    private final GoogleMapsService googleMapsService;

    @Override
    @Transactional
    public Solicitacao criarSolicitacaoEGerarRotas(SolicitacaoRequestDTO request) {
        // Busca as entidades principais
        Crianca crianca = criancaRepository.findById(request.getDependenteId())
                .orElseThrow(() -> new RuntimeException("Dependente (Criança) não encontrado."));

        Motoristas motorista = motoristasRepository.findById(request.getMotoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado."));

        // PASSO 1: Criar a Solicitacao
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setCrianca(crianca);
        solicitacao.setMotorista(motorista);
        solicitacao.setStatus("PENDENTE");

        Solicitacao savedSolicitacao = solicitacaoRepository.save(solicitacao);

        // PASSO 2: Calcular Rotas Sugestão
        TipoServico tipo = crianca.getTipoServico();
        Escolas escola = crianca.getEscola();

        List<Rota> rotasSugeridas = new ArrayList<>();

        // A) Cálculo da Rota de IDA
        if (tipo == TipoServico.IDA || tipo == TipoServico.IDA_E_VOLTA) {
            rotasSugeridas.add(
                    calcularERotaSugerida(savedSolicitacao, motorista, crianca, escola, TipoServico.IDA)
            );
        }
        // B) Cálculo da Rota de VOLTA
        if (tipo == TipoServico.VOLTA || tipo == TipoServico.IDA_E_VOLTA) {
            rotasSugeridas.add(
                    calcularERotaSugerida(savedSolicitacao, motorista, crianca, escola, TipoServico.VOLTA)
            );
        }

        // Atualiza a solicitação com as rotas criadas
        savedSolicitacao.setRotasSugeridas(rotasSugeridas);
        return savedSolicitacao;
    }

    /**
     * PASSO 2 (A MÁGICA) - Método privado para calcular uma rota (IDA ou VOLTA)
     */
    private Rota calcularERotaSugerida(Solicitacao solicitacao, Motoristas motorista, Crianca novoAluno, Escolas escola, TipoServico tipo) {

        // 1. Definir Origem, Destino e Waypoints (Alunos)
        String origem;
        String destino;
        List<String> waypoints = new ArrayList<>();

        // Adiciona o endereço do Motorista (Ponto de partida/chegada)
        if (motorista.getUser() == null || motorista.getUser().getEndereco() == null || motorista.getUser().getEndereco().isEmpty()) {
            throw new IllegalStateException("Motorista com ID " + motorista.getId() + " não possui um endereço de usuário válido para o cálculo da rota.");
        }
        String enderecoMotorista = motorista.getUser().getEndereco();
        String enderecoEscola = escola.getEndereco();

        // 2. Query Principal (Alunos Antigos)
        List<TipoServico> tiposServicoQuery;

        // Configura a rota baseado no tipo (IDA ou VOLTA)
        if (tipo == TipoServico.IDA) {
            origem = enderecoMotorista;
            destino = enderecoEscola;
            tiposServicoQuery = List.of(TipoServico.IDA, TipoServico.IDA_E_VOLTA);
        } else {
            origem = enderecoEscola;
            destino = enderecoMotorista;
            tiposServicoQuery = List.of(TipoServico.VOLTA, TipoServico.IDA_E_VOLTA);
        }

        // Busca os alunos antigos que se encaixam na rota
        List<Crianca> alunosAntigos = criancaRepository.findAlunosAceitosParaRota(
                motorista,
                escola,
                tiposServicoQuery
        );

        // 3. Coletar Pontos (Waypoints)
        // Adiciona o novo aluno
        waypoints.add(novoAluno.getEndereco());
        // Adiciona os alunos antigos
        for (Crianca antigo : alunosAntigos) {
            waypoints.add(antigo.getEndereco());
        }

        // 4. API Externa (Google Maps) - AGORA É REAL
        ResultadoOtimizadoDTO resultadoGoogle = googleMapsService.otimizarRota(origem, destino, waypoints);

        // Extrai os dados reais da resposta
        BigDecimal distanciaKm = resultadoGoogle.getDistanciaKm();
        Integer tempoMin = resultadoGoogle.getTempoMin();
        List<Ponto> pontosOrdenados = resultadoGoogle.getPontosOrdenados();

        // 5. Salvar Rota
        Rota rotaSugerida = new Rota();
        rotaSugerida.setNomeRota(tipo == TipoServico.IDA ? "Sugestão Rota de IDA" : "Sugestão Rota de VOLTA");
        rotaSugerida.setDistanciaKm(distanciaKm);
        rotaSugerida.setTempoEstimadoMin(tempoMin);
        rotaSugerida.setSolicitacao(solicitacao);
        Rota rotaSalva = rotaRepository.save(rotaSugerida);

        // 6. Salvar Pontos
        for (Ponto ponto : pontosOrdenados) {
            ponto.setRota(rotaSalva);
            pontoRepository.save(ponto);
        }

        rotaSugerida.setPontos(pontosOrdenados);
        return rotaSalva;
    }

    /**
     * PASSO 4: O Motorista toma a decisão
     */
    @Override
    @Transactional
    public Solicitacao decidirSolicitacao(UUID solicitacaoId, DecisaoRequestDTO request) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada."));

        String decisao = request.getStatus().toUpperCase();
        solicitacao.setStatus(decisao); // "ACEITO" ou "RECUSADO"

        // Ação Opcional (Limpeza): Se RECUSADO
        if ("RECUSADO".equals(decisao)) {
            List<Rota> rotasParaApagar = solicitacao.getRotasSugeridas();
            rotaRepository.deleteAll(rotasParaApagar);
            solicitacao.setRotasSugeridas(null);
        }

        // Ação Opcional (Contrato): Se ACEITO
        if ("ACEITO".equals(decisao)) {
            Contrato novoContrato = new Contrato();
            novoContrato.setMotorista(solicitacao.getMotorista());
            novoContrato.setResponsavel(solicitacao.getCrianca().getResponsavel());
            novoContrato.setDataInicio(LocalDate.now());

            // Implementando a regra de negócio
            novoContrato.setDataFim(LocalDate.now().plusYears(1)); // Duração de 1 ano
            novoContrato.setValorMensal(new BigDecimal("100.00")); // Valor fixo de R$ 100,00

            contratoRepository.save(novoContrato);
        }

        return solicitacaoRepository.save(solicitacao);
    }

    /**
     * MÉTODO (O que faltava)
     */
    @Override
    public Optional<Solicitacao> findById(UUID id) {
        return solicitacaoRepository.findById(id);
    }

    // --- CORREÇÃO: O método duplicado "criarSolicitacaoComRotas" foi removido ---
}