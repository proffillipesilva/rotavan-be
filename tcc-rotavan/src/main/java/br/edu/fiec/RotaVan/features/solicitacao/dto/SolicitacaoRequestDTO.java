package br.edu.fiec.RotaVan.features.solicitacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.UUID;

@Data
@Schema(description = "DTO para a criação de uma nova solicitação de vaga.")
public class SolicitacaoRequestDTO {

    @Schema(description = "ID (UUID) do Responsável.", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID responsavelId;

    @Schema(description = "ID (UUID) do Motorista.", example = "b1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID motoristaId;

    @Schema(description = "ID (UUID) da Criança.", example = "c1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID criancaId;

    @Schema(description = "ID (UUID) da Escola.", example = "d1b2c3d4-e5f6-7890-1234-567890abcdef")
    private UUID escolaId;
}