package br.edu.fiec.RotaVan.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.UUID;

@Data
@Schema(description = "DTO (Data Transfer Object) para registrar um novo dependente (criança) associado a um responsável.")
public class CriancaRegisterDTO {

    @Schema(description = "Nome completo do dependente.",
            example = "Enzo Gabriel da Silva",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "ID (UUID) da escola onde o dependente estuda.",
            example = "e1a2b3c4-d5e6-f789-0123-456789abcdef",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID escolaId;
}