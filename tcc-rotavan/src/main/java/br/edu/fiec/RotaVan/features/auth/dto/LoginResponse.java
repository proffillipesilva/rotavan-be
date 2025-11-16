package br.edu.fiec.RotaVan.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO (Data Transfer Object) para a resposta de login/registro.")
public class LoginResponse {

    @Schema(description = "Token JWT (Bearer token) gerado para autenticação.",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3V...")
    String token;
}