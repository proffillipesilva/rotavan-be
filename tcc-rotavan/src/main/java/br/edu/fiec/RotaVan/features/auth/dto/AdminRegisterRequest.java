package br.edu.fiec.RotaVan.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO (Data Transfer Object) para o registro de um novo usuário Admin.")
public class AdminRegisterRequest {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    @Schema(description = "Nome completo do administrador.",
            example = "Admin RotaVan",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Forneça um endereço de email válido")
    @Schema(description = "Email de login do administrador.",
            example = "admin@rotavan.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha de acesso (mínimo 6 caracteres).",
            example = "senhaforte123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}