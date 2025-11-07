package br.edu.fiec.RotaVan.features.solicitacao.services;

import br.edu.fiec.RotaVan.features.solicitacao.dto.DecisaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.dto.SolicitacaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;

import java.util.Optional;
import java.util.UUID;

public interface SolicitacaoService {
    // PASSO 1 e 2
    Solicitacao criarSolicitacaoEGerarRotas(SolicitacaoRequestDTO request);

    // PASSO 4
    Solicitacao decidirSolicitacao(UUID solicitacaoId, DecisaoRequestDTO request);

    Optional<Solicitacao> findById(UUID id);
}