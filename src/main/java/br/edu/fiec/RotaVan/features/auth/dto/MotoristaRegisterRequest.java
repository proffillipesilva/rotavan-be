package br.edu.fiec.RotaVan.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MotoristaRegisterRequest {

    // Dados para o login (User)
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Forneça um endereço de email válido")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    // Dados para o perfil (Motoristas)
    @NotBlank(message = "O nome do motorista não pode estar em branco")
    private String nomeMotorista;

    @NotNull(message = "A CNH não pode ser nula")
    private Long cnh;

    @NotNull(message = "O CPF não pode ser nulo")
    private Long cpf;

    @NotBlank(message = "A placa do veículo não pode estar em branco")
    private String placaVeiculo;
}