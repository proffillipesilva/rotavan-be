package br.edu.fiec.RotaVan.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Motoristas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    String nomeMotorista;

    @Column(nullable = false)
    Long cnh;

    @Column(nullable = false)
    Long cpf;

    @Column(nullable = false)
    String placaVeiculo;

}
