package br.edu.fiec.RotaVan.features.solicitacao.services;

import br.edu.fiec.RotaVan.features.solicitacao.dto.DecisaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.dto.SolicitacaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID; // Importante: UUID

public interface SolicitacaoService {

    Solicitacao criarSolicitacaoEGerarRotas(SolicitacaoRequestDTO request);

    // Alterado de Integer para UUID
    Optional<Solicitacao> findById(UUID id);

    // Alterado de Integer para UUID
    Solicitacao decidirSolicitacao(UUID solicitacaoId, DecisaoRequestDTO decisao);

    List<Solicitacao> listarTodas();
}