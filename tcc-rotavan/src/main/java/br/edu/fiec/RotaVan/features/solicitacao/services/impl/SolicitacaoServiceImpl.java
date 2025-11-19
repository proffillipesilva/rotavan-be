package br.edu.fiec.RotaVan.features.solicitacao.services.impl;

import br.edu.fiec.RotaVan.features.contratos.services.ContratoService;
import br.edu.fiec.RotaVan.features.firebase.dto.NotificationMessage; // <-- Importante: Importar o DTO
import br.edu.fiec.RotaVan.features.firebase.services.NotificationService;
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
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.repositories.CriancaRepository;
import br.edu.fiec.RotaVan.features.user.repositories.EscolasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.features.veiculos.repositories.VeiculoRepository;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private static final Logger log = LoggerFactory.getLogger(SolicitacaoServiceImpl.class);

    private final SolicitacaoRepository solicitacaoRepository;
    private final ResponsaveisRepository responsavelRepository;
    private final MotoristasRepository motoristaRepository;
    private final CriancaRepository criancaRepository;
    private final EscolasRepository escolaRepository;
    private final GoogleMapsService googleMapsService;
    private final RotaRepository rotaRepository;
    private final PontoRepository pontoRepository;
    private final VeiculoRepository veiculoRepository;
    private final ContratoService contratoService;
    private final NotificationService notificationService;

    @Override
    public Solicitacao criarSolicitacaoEGerarRotas(SolicitacaoRequestDTO request) {
        var responsavel = responsavelRepository.findById(request.getResponsavelId())
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado"));
        var motorista = motoristaRepository.findById(request.getMotoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado"));
        var crianca = criancaRepository.findById(request.getCriancaId())
                .orElseThrow(() -> new RuntimeException("Criança não encontrada"));
        var escola = escolaRepository.findById(request.getEscolaId())
                .orElseThrow(() -> new RuntimeException("Escola não encontrada"));

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setResponsavel(responsavel);
        solicitacao.setMotorista(motorista);
        solicitacao.setCrianca(crianca);
        solicitacao.setEscola(escola);
        solicitacao.setStatus("PENDENTE");

        solicitacao = solicitacaoRepository.save(solicitacao);

        // Lógica de rotas sugeridas aqui...

        return solicitacaoRepository.save(solicitacao);
    }

    @Override
    public Optional<Solicitacao> findById(UUID id) {
        return solicitacaoRepository.findById(id);
    }

    @Override
    public Solicitacao decidirSolicitacao(UUID solicitacaoId, DecisaoRequestDTO decisao) {
        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        Motoristas motorista = solicitacao.getMotorista();
        Responsaveis responsavel = solicitacao.getResponsavel();
        Crianca crianca = solicitacao.getCrianca();

        if ("ACEITA".equals(decisao.getDecisao())) {
            solicitacao.setStatus("ACEITA");

            if (solicitacao.getRotasSugeridas() != null) {
                for (Rota rotaSugerida : solicitacao.getRotasSugeridas()) {
                    rotaSugerida.setTipo("OFICIAL");
                    rotaRepository.save(rotaSugerida);

                    if (rotaSugerida.getPontos() != null) {
                        for (Ponto ponto : rotaSugerida.getPontos()) {
                            ponto.setRota(rotaSugerida);
                            pontoRepository.save(ponto);
                        }
                    }
                }
            }

            // Criar Contrato
            contratoService.criarContrato(responsavel, motorista, crianca);

            // --- CORREÇÃO 1: Construir o objeto NotificationMessage ---
            // --- CORREÇÃO 2: Usar responsavel.getUser().getId() ---
            // --- CORREÇÃO 3: Usar motorista.getNomeMotorista() ---
            enviarNotificacaoSegura(
                    responsavel.getUser().getId(), // ID do USER, não do Responsável
                    "Solicitação Aceita!",
                    "O motorista " + motorista.getNomeMotorista() + " aceitou o transporte."
            );

        } else if ("RECUSADA".equals(decisao.getDecisao())) {
            solicitacao.setStatus("RECUSADA");

            enviarNotificacaoSegura(
                    responsavel.getUser().getId(), // ID do USER, não do Responsável
                    "Solicitação Recusada",
                    "O motorista não pode aceitar o transporte neste momento."
            );
        } else {
            throw new IllegalArgumentException("Decisão inválida. Use ACEITA ou RECUSADA.");
        }

        return solicitacaoRepository.save(solicitacao);
    }

    @Override
    public List<Solicitacao> listarTodas() {
        return solicitacaoRepository.findAll();
    }

    // Método auxiliar para tratar erros de notificação sem parar o fluxo principal
    private void enviarNotificacaoSegura(UUID userId, String titulo, String mensagem) {
        try {
            NotificationMessage msg = NotificationMessage.builder()
                    .userId(userId.toString())
                    .title(titulo)
                    .message(mensagem)
                    .build();

            notificationService.sendNotificationToUser(msg);
        } catch (Exception e) {
            // Apenas loga o erro, não impede a solicitação de ser salva
            log.error("Falha ao enviar notificação para o usuário {}: {}", userId, e.getMessage());
        }
    }
}