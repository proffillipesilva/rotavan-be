package br.edu.fiec.RotaVan.features.solicitacao.services;

import br.edu.fiec.RotaVan.features.solicitacao.dto.DecisaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.dto.SolicitacaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;

import java.util.Optional;
import java.util.UUID;

public interface SolicitacaoService {

    // Este é o método correto que implementamos
    Solicitacao criarSolicitacaoEGerarRotas(SolicitacaoRequestDTO request);

    // Este método é para o motorista tomar a decisão
    Solicitacao decidirSolicitacao(UUID solicitacaoId, DecisaoRequestDTO request);

    // Este método é usado pelo controller para buscar as rotas
    Optional<Solicitacao> findById(UUID id);

    // --- CORREÇÃO: O método duplicado "criarSolicitacaoComRotas" foi removido ---
}