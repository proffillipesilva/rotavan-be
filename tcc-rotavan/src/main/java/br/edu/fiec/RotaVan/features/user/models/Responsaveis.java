package br.edu.fiec.RotaVan.features.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Schema(description = "Representa os dados específicos de um Responsável, complementando a entidade User.")
public class Responsaveis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do responsável (UUID).",
            example = "r1a2b3c4-d5e6-f789-0123-456789resp")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    @Schema(description = "Referência interna ao usuário (User) associado (ignorado no JSON).")
    private User user;

    @Column(nullable = false)
    @Schema(description = "Nome completo do responsável (deve ser o mesmo do User).",
            example = "Maria da Silva",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeResponsavel;

    @Column(nullable = false, unique = true)
    @Schema(description = "CPF do responsável (somente números).",
            example = "11122233344",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String cpfResponsavel;

    @Column(nullable = false)
    @Schema(description = "Endereço residencial principal do responsável.",
            example = "Rua das Flores, 123, Bairro Feliz, Indaiatuba-SP",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String enderecoCasa;

    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de dependentes associados a este responsável.")
    private List<Crianca> criancas;

}