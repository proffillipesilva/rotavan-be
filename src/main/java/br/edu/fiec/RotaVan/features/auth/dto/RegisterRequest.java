package br.edu.fiec.RotaVan.features.auth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String nomeResponsavel;
    private String email;
    private String password;
    private String cpfResponsavel;
    private String endrecoCasa;
    private String nomeCrianca;
    // Opcionalmente, pode-se incluir o nível de acesso
    // private UserLevel accessLevel;
}
