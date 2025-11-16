package br.edu.fiec.RotaVan.features.auth.dto;

import br.edu.fiec.RotaVan.features.user.models.TipoServico; // <-- 1. IMPORTAR
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal; // <-- 2. IMPORTAR
import java.time.LocalDate;  // <-- 3. IMPORTAR
import java.util.UUID;

@Data
@Schema(description = "DTO para registar um novo dependente (criança) durante o registo do responsável.")
public class CriancaRegisterDTO {

    @NotBlank(message = "O nome da criança não pode estar em branco")
    @Schema(description = "Nome completo do dependente.",
            example = "Enzo Gabriel da Silva",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @NotNull(message = "O ID da escola não pode ser nulo")
    @Schema(description = "ID (UUID) da escola onde o dependente estuda.",
            example = "e1a2b3c4-d5e6-f789-0123-456789abcdef",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID escolaId;

    // --- CAMPOS ADICIONADOS (SINCRONIZANDO COM Crianca.java) ---

    @Schema(description = "Data de nascimento do dependente.",
            example = "2015-08-20")
    private LocalDate dataNascimento;

    @Schema(description = "Nível de ensino do dependente.",
            example = "Ensino Fundamental I")
    private String nivelEscolar;

    @Schema(description = "Endereço residencial do dependente (se for diferente do responsÃ¡vel).",
            example = "Rua das Palmeiras, 456, Apto 12")
    private String endereco;

    @Schema(description = "Define o tipo de serviço de transporte contratado.",
            example = "IDA_E_VOLTA")
    private TipoServico tipoServico;

    @Schema(description = "Período em que o dependente estuda (ex: MANHA, TARDE).",
            example = "MANHA")
    private String periodo;

    @Schema(description = "Coordenada de Latitude (geolocalização).",
            example = "-23.10000000")
    private BigDecimal latitude;

    @Schema(description = "Coordenada de Longitude (geolocalização).",
            example = "-47.20000000")
    private BigDecimal longitude;
    // --- FIM DA ADIÇÃO ---
}