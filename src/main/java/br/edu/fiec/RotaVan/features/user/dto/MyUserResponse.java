package br.edu.fiec.RotaVan.features.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyUserResponse {
    private UUID userId;
    private String nome;
    private String email;
    private String role;
    private String picture;

    // Campos de Responsável
    private String cpfResponsavel;
    private String enderecoCasa;

    // --- CORREÇÃO AQUI ---
    // Campos de Motorista
    private String cnh; // Alterado de Long para String
    private String cpfMotorista; // Alterado de Long para String
    // O campo placaVeiculo foi removido, pois agora pertence à entidade Veiculo
}