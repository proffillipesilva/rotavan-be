package br.edu.fiec.RotaVan.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate; // IMPORTAR

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

    @NotBlank(message = "A CNH não pode estar em branco") // MUDADO DE @NotNull para @NotBlank
    private String cnh; // MUDADO DE Long para String

    @NotBlank(message = "O CPF não pode estar em branco") // MUDADO DE @NotNull para @NotBlank
    private String cpf; // MUDADO DE Long para String

    @NotBlank(message = "A placa do veículo não pode estar em branco")
    private String placaVeiculo;

    // --- CAMPO ADICIONADO ---
    @NotNull(message = "A data de validade da CNH não pode ser nula")
    private LocalDate valCnh;
    // --- FIM DA ADIÇÃO ---
}