package br.edu.fiec.RotaVan.features.auth.dto;

import br.edu.fiec.RotaVan.features.auth.dto.CriancaRegisterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "DTO (Data Transfer Object) para o registro de um novo usuário Responsável.")
public class RegisterRequest {
    @NotBlank(message = "O nome do responsável não pode estar em branco")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    @Schema(description = "Nome completo do responsável.",
            example = "Maria da Silva",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeResponsavel;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Forneça um endereço de email válido")
    @Schema(description = "Email de login do responsável.",
            example = "maria.silva@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha de acesso (mínimo 6 caracteres).",
            example = "senhaforte123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "O CPF não pode estar em branco")
    @Schema(description = "CPF do responsável (somente números).",
            example = "11122233344",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String cpfResponsavel;

    @NotBlank(message = "O endereço nao pode estar em branco")
    @Schema(description = "Endereço residencial principal do responsável.",
            example = "Rua das Flores, 123, Bairro Feliz, Indaiatuba-SP",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String enderecoCasa;

    @Schema(description = "Lista de dependentes (crianças) para cadastrar junto com o responsável. (Opcional no registro inicial)")
    private List<CriancaRegisterDTO> criancas;
}