package br.edu.fiec.RotaVan.features.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "DTO (Data Transfer Object) para o registo de um novo utilizador Motorista.")
public class MotoristaRegisterRequest {

    // Dados para o login (User)
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Forneça um endereço de email válido")
    @Schema(description = "Email de login do motorista.",
            example = "motorista@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Schema(description = "Senha de acesso (mínimo 6 caracteres).",
            example = "senhaforte123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    // Dados para o perfil (Motoristas)
    @NotBlank(message = "O nome do motorista não pode estar em branco")
    @Schema(description = "Nome completo do motorista.",
            example = "Carlos Alberto",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeMotorista;

    @NotBlank(message = "A CNH não pode estar em branco")
    @Schema(description = "Número da CNH do motorista.",
            example = "01234567890",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String cnh;

    @NotBlank(message = "O CPF não pode estar em branco")
    @Schema(description = "CPF do motorista (somente números).",
            example = "99988877766",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String cpf;

    @NotBlank(message = "A placa do veículo não pode estar em branco")
    @Schema(description = "Placa do veículo principal do motorista (formato Mercosul ou antigo).",
            example = "BRA2E19",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String placaVeiculo;

    @NotNull(message = "A data de validade da CNH não pode ser nula")
    @Schema(description = "Data de validade da CNH do motorista.",
            example = "2028-10-20",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate valCnh;

    // --- CAMPO ADICIONADO PARA O ENDEREÇO ---
    @NotBlank(message = "O endereço não pode estar em branco")
    @Schema(description = "Endereço residencial do motorista (ponto de partida).",
            example = "Rua das Oficinas, 200, Indaiatuba-SP",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String endereco;
    // ----------------------------------------
}