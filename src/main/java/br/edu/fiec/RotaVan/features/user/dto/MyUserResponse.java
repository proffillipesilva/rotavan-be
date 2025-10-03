package br.edu.fiec.RotaVan.features.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Opcional: omite campos nulos no JSON
public class MyUserResponse {
    private UUID userId;
    private String nome;
    private String email;
    private String role;
    private String picture;

    // Campos de Responsável
    private String cpfResponsavel;
    private String enderecoCasa;

    // Campos de Motorista
    private Long cnh;
    private Long cpfMotorista;
    private String placaVeiculo;
}