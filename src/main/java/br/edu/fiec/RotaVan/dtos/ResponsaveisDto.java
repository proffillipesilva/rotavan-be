package br.edu.fiec.RotaVan.dtos;

import lombok.Data;

@Data
public class ResponsaveisDto {

    private String email;

    private String password;

    private String nomeResponsavel;

    private Long cpfResponsavel;

    private String enderecoCasa;

    private String nomeCrianca;

    private String endrecoEscola;

}
