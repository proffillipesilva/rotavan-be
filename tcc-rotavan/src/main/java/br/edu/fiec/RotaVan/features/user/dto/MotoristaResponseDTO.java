package br.edu.fiec.RotaVan.features.user.dto; // Ou o teu pacote de DTOs

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class MotoristaResponseDTO {

    // Dados do Perfil Motoristas
    private UUID motoristaId; // ID do perfil motorista
    private String nomeMotorista;
    private String cnh;
    private LocalDate valCnh;

    // Dados da Entidade User
    private UUID userId; // ID do usuário (login)
    private String email;

    /**
     * Método "Mapper" estático.
     * Converte uma entidade JPA 'Motoristas' para o nosso DTO de resposta.
     * * @param motorista A entidade 'Motoristas' vinda do banco de dados.
     * @return Um DTO 'MotoristaResponseDTO' com dados seguros para a API.
     */
    public static MotoristaResponseDTO fromEntity(Motoristas motorista) {
        MotoristaResponseDTO dto = new MotoristaResponseDTO();

        // Mapeia os dados do perfil 'Motoristas'
        dto.setMotoristaId(motorista.getId());
        dto.setNomeMotorista(motorista.getNomeMotorista());
        dto.setCnh(motorista.getCnh());
        dto.setValCnh(motorista.getValCnh());

        // Mapeia os dados do 'User' associado
        // É seguro fazer get(), desde que o 'user' não esteja nulo
        if (motorista.getUser() != null) {
            dto.setUserId(motorista.getUser().getId());
            dto.setEmail(motorista.getUser().getEmail());
        }

        return dto;
    }
}