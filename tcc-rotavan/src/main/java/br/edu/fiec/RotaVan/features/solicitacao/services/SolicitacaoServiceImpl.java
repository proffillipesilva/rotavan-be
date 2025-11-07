package br.edu.fiec.RotaVan.features.solicitacao.services;

import br.edu.fiec.RotaVan.features.contratos.models.Contrato;
import br.edu.fiec.RotaVan.features.contratos.repositories.ContratoRepository;
import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.repositories.PontoRepository;
import br.edu.fiec.RotaVan.features.rotas.repositories.RotaRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor // Injeta todos os Repositories automaticamente
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final CriancaRepository criancaRepository;
    private final MotoristasRepository motoristasRepository;
    private final ResponsaveisRepository responsaveisRepository; // Necessário para Contrato Opcional
    private final RotaRepository rotaRepository;
    private final PontoRepository pontoRepository;
    private final ContratoRepository contratoRepository; // Para o Passo 4 Opcional

    // TODO: Você precisará criar e injetar um serviço para o Google Maps
    // private final GoogleMapsService googleMapsService;

    @Override
    @Transactional
    public Solicitacao criarSolicitacaoEGerarRotas(SolicitacaoRequestDTO request) {
        // Busca as entidades principais
        Crianca crianca = criancaRepository.findById(request.getDependenteId())
                .orElseThrow(() -> new RuntimeException("Dependente (Criança) não encontrado."));

        Motoristas motorista = motoristasRepository.findById(request.getMotoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado."));

        // PASSO 1: Criar a Solicitacao (conforme o guia)
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setCrianca(crianca);
        solicitacao.setMotorista(motorista);
        solicitacao.setStatus("PENDENTE");
        // Salva primeiro para obter o ID
        Solicitacao savedSolicitacao = solicitacaoRepository.save(solicitacao);

        // PASSO 2: Calcular Rotas Sugestão
        TipoServico tipo = crianca.getTipoServico();
        Escolas escola = crianca.getEscola();

        // Lista para armazenar as rotas criadas
        List<Rota> rotasSugeridas = new ArrayList<>();

        if (tipo == TipoServico.IDA || tipo == TipoServico.IDA_E_VOLTA) {
            rotasSugeridas.add(
                    calcularERotaSugerida(savedSolicitacao, motorista, crianca, escola, TipoServico.IDA)
            );
        }
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

        // 1. Coletar Pontos (Conforme o guia "Como a Rota é Montada")
        List<String> enderecosParaGoogle = new ArrayList<>();

        // TODO: Adicionar endereço do Motorista (ex: motorista.getUser().getEndereco())
        // enderecosParaGoogle.add(motorista.getEnderecoDaCasa());

        // Adiciona endereço da Escola
        enderecosParaGoogle.add(escola.getEndereco());

        // Adiciona endereço do Novo Aluno
        enderecosParaGoogle.add(novoAluno.getEndereco());

        // 2. Query Principal (Alunos Antigos - Conforme o guia)
        // TODO: Você DEVE criar este método no seu CriancaRepository
        // List<Crianca> alunosAntigos = criancaRepository.findAlunosAceitosParaRota(motorista, escola, tipo);
        // for (Crianca antigo : alunosAntigos) {
        //    enderecosParaGoogle.add(antigo.getEndereco());
        // }

        // 3. API Externa (Google Maps)
        // TODO: Substituir esta simulação pela chamada real ao seu GoogleMapsService
        // ResultadoSimulado resultadoGoogle = googleMapsService.otimizarRota(enderecosParaGoogle, tipo);

        // --- SIMULAÇÃO (Remover quando implementar o Google) ---
        double distanciaSimulada = (tipo == TipoServico.IDA) ? 18.5 : 19.0;
        int tempoSimulado = (tipo == TipoServico.IDA) ? 40 : 42;
        // Simula o Google retornando os pontos na ordem
        List<String> pontosOrdenadosSimulados = new ArrayList<>(enderecosParaGoogle);
        // --- Fim da Simulação ---


        // 4. Salvar Rota (Conforme o guia)
        Rota rotaSugerida = new Rota();
        rotaSugerida.setNomeRota(tipo == TipoServico.IDA ? "Sugestão Rota de IDA" : "Sugestão Rota de VOLTA");
        rotaSugerida.setDistanciaKm(BigDecimal.valueOf(distanciaSimulada));
        rotaSugerida.setTempoEstimadoMin(tempoSimulado);
        rotaSugerida.setSolicitacao(solicitacao); // LIGA A ROTA À SOLICITACAO (fk_Solicitacao)
        Rota rotaSalva = rotaRepository.save(rotaSugerida);

        // 5. Salvar Pontos (Conforme o guia)
        int ordem = 1;
        for (String endereco : pontosOrdenadosSimulados) {
            Ponto ponto = new Ponto();
            // TODO: O Google Maps retornará Lat/Lng reais. Por enquanto, usamos valores fictícios.
            ponto.setLatitude(BigDecimal.valueOf(-23.5000));
            ponto.setLongitude(BigDecimal.valueOf(-46.6000));
            ponto.setNomePonto(endereco); // O nome do ponto pode ser o endereço
            ponto.setOrdem(ordem++);
            ponto.setRota(rotaSalva); // LIGA O PONTO À ROTA (fk_Rota)
            pontoRepository.save(ponto);
        }

        rotaSalva.setPontos(pontoRepository.findAll()); // TODO: Refinar para buscar só os pontos desta rota
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

        // Atualiza o Status
        solicitacao.setStatus(request.getStatus().toUpperCase()); // "ACEITO" ou "RECUSADO"

        // Ação Opcional (Limpeza): Se RECUSADO
        if ("RECUSADO".equals(solicitacao.getStatus())) {
            // Se 'orphanRemoval = true' estiver na entidade Solicitacao no campo 'rotasSugeridas',
            // o JPA apaga as rotas automaticamente ao setar a lista como nula ou vazia.
            // Se não, fazemos manualmente (como no guia):

            // 1. Busca as rotas ligadas a esta solicitação
            List<Rota> rotasParaApagar = solicitacao.getRotasSugeridas();

            // 2. Apaga (O ON DELETE CASCADE no BD deve apagar os Pontos)
            rotaRepository.deleteAll(rotasParaApagar);

            // Limpa a lista na entidade
            solicitacao.setRotasSugeridas(null);
        }

        // Ação Opcional (Contrato): Se ACEITO (conforme o guia)
        if ("ACEITO".equals(solicitacao.getStatus())) {
            // TODO: Implementar a lógica de criação de contrato
            // Contrato novoContrato = new Contrato();
            // novoContrato.setMotorista(solicitacao.getMotorista());
            // novoContrato.setResponsavel(solicitacao.getCrianca().getResponsavel());
            // novoContrato.setDataInicio(LocalDate.now());
            // ... (setar dataFim, valorMensal)
            // contratoRepository.save(novoContrato);
        }

        return solicitacaoRepository.save(solicitacao);
    }

    /**
     * MÉTODO CORRIGIDO (O que faltava)
     */
    @Override
    public Optional<Solicitacao> findById(UUID id) {
        return solicitacaoRepository.findById(id);
    }
}