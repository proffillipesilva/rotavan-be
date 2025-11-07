package br.edu.fiec.RotaVan.features.solicitacao.dto;

import lombok.Data;
import java.util.UUID;

// DTO para o PASSO 1 (O pedido do Responsável)
@Data
public class SolicitacaoRequestDTO {
    private UUID motoristaId;
    private UUID dependenteId; // (CriancaId)
}