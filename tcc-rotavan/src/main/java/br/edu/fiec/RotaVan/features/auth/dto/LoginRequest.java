package br.edu.fiec.RotaVan.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO (Data Transfer Object) para a requisição de login.")
public class LoginRequest {

    @Schema(description = "Email do usuário cadastrado.",
            example = "usuario@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "Senha de acesso do usuário.",
            example = "senha123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}