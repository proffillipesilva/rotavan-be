package br.edu.fiec.RotaVan.features.solicitacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// DTO para o PASSO 3 (Decisão do Motorista)
@Data
@Schema(description = "DTO (Data Transfer Object) para o Motorista enviar a decisão (Aceitar/Recusar) de uma solicitação.")
public class DecisaoRequestDTO {

    @Schema(description = "A decisão do motorista sobre a solicitação.",
            example = "ACEITO",
            allowableValues = {"ACEITO", "RECUSADO"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status; // "ACEITO" ou "RECUSADO"
}