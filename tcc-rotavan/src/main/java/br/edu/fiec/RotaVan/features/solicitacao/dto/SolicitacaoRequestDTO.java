package br.edu.fiec.RotaVan.features.solicitacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.UUID;

// DTO para o PASSO 1 (O pedido do Responsável)
@Data
@Schema(description = "DTO (Data Transfer Object) para a criação de uma nova solicitação de vaga (Passo 1).")
public class SolicitacaoRequestDTO {

    @Schema(description = "ID (UUID) do Motorista que o responsável quer contratar.",
            example = "m1a2b3c4-d5e6-f789-0123-456789motor",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID motoristaId;

    @Schema(description = "ID (UUID) do Dependente (Criança) que precisa do transporte.",
            example = "c1b2a3d4-e5f6-7890-1234-567890abcdef",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID dependenteId; // (CriancaId)
}