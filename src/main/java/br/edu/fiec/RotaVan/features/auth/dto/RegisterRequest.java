package br.edu.fiec.RotaVan.features.auth.dto;

import lombok.Data;
import java.util.List;

@Data
public class RegisterRequest {
    private String nomeResponsavel;
    private String email;
    private String password;
    private String cpfResponsavel;
    private String enderecoCasa;
    private List<CriancaRegisterDTO> criancas;
}