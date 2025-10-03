package br.edu.fiec.RotaVan.features.auth.dto;

import br.edu.fiec.RotaVan.features.auth.dto.CriancaRegisterDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class RegisterRequest {
    @NotBlank(message = "O nome do responsável não pode estar em branco")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    private String nomeResponsavel;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Forneça um endereço de email válido")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "O CPF não pode estar em branco")
    private String cpfResponsavel;

    @NotBlank(message = "O endereço não pode estar em branco")
    private String enderecoCasa;

    private List<CriancaRegisterDTO> criancas;
}