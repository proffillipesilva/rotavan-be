package br.edu.fiec.RotaVan.features.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema; // 1. IMPORTAR O SCHEMA
import lombok.Data;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Representa a resposta de dados do usuário logado (endpoint /me)")
public class MyUserResponse {

    @Schema(description = "ID único do usuário (UUID).",
            example = "a1b2c3d4-e5f6-7890-1234-567890abcdef",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID userId;

    @Schema(description = "Nome completo do usuário.",
            example = "Maria Souza",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "Email de login do usuário.",
            example = "maria.souza@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Nível de acesso do usuário.",
            example = "RESPONSAVEL", // ou "MOTORISTA"
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;

    @Schema(description = "Chave S3 (ou URL completa) da foto de perfil do usuário.",
            example = "profiles/a1b2c3d4-e5f6-foto.jpg")
    private String picture;

    // --- Campos de Responsável ---
    // (Estes campos só virão se o 'role' for RESPONSAVEL)

    @Schema(description = "CPF do responsável (somente números).",
            example = "11122233344")
    private String cpfResponsavel;

    @Schema(description = "Endereço residencial do responsável.",
            example = "Rua das Flores, 123, Bairro Feliz, Indaiatuba-SP")
    private String enderecoCasa;

    // --- Campos de Motorista ---
    // (Estes campos só virão se o 'role' for MOTORISTA)

    @Schema(description = "Número da Carteira Nacional de Habilitação (CNH).",
            example = "01234567890")
    private String cnh;

    @Schema(description = "CPF do motorista (somente números).",
            example = "99988877766")
    private String cpfMotorista;
}