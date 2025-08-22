package br.edu.fiec.RotaVan.dtos;

import lombok.Data;

@Data
public class MotoristasDto {


    private String email;

    private String password;

    private String nomeMotorista;

    private Long cnh;

    private Long cpf;

    private Long placaVeiculo;

}
