package br.edu.fiec.RotaVan.features.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email; // <-- IMPORTAR
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO (Data Transfer Object) para o registo de uma nova Escola (com login).") // <-- Descrição atualizada
public class EscolaRegisterRequest {

    // --- ADICIONADO: Campos de Login (para User) ---
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Forneça um endereço de email válido")
    @Schema(description = "Email de login da escola.",
            example = "contato@escolaexemplo.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha de acesso (mínimo 6 caracteres).",
            example = "senhaforte123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String senha;
    // --- FIM DA ADIÇÃO ---

    // --- Campos de Perfil (para Escolas) ---
    @NotBlank(message = "O nome da escola não pode estar em branco")
    @Schema(description = "Nome oficial da escola.",
            example = "Colégio Exemplo de Indaiatuba",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @NotBlank(message = "O CNPJ não pode estar em branco")
    @Size(min = 14, max = 14, message = "O CNPJ deve ter 14 dígitos")
    @Schema(description = "CNPJ da escola (somente números).",
            example = "12345678000199",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String cnpj;

    @NotBlank(message = "O endereço não pode estar em branco")
    @Schema(description = "Endereço completo da escola.",
            example = "Avenida Principal, 1000, Centro, Indaiatuba-SP",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String endereco;
}

//    @Schema(description = "Telefone principal de contato da escola (Opcional).",
//            example = "1938887766")
//    private String telefone;
//}