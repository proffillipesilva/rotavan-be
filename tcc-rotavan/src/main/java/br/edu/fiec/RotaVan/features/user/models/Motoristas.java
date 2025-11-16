package br.edu.fiec.RotaVan.features.user.models;

// --- IMPORTAÇÕES ADICIONADAS/REVISADAS ---
import br.edu.fiec.RotaVan.features.veiculos.models.Veiculo; // Importar Veiculo
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema; // <-- SÓ ADICIONEI ISSO
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List; // Importar List
import java.util.UUID;
// --- FIM DAS IMPORTAÇÕES ---

@Entity
// @Table(name = "motoristas") // Opcional
@Data
@Schema(description = "Representa os dados específicos de um Motorista, complementando a entidade User.") // <-- ADICIONEI
public class Motoristas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do motorista (UUID).", // <-- ADICIONEI
            example = "m1a2b3c4-d5e6-f789-0123-456789motor")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    @Schema(description = "Referência interna ao usuário (User) associado (ignorado no JSON).") // <-- ADICIONEI
    private User user;

    @Column(nullable = false)
    @Schema(description = "Nome completo do motorista (deve ser o mesmo do User).", // <-- ADICIONEI
            example = "Carlos Alberto",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeMotorista;

    @Column(nullable = false)
    @Schema(description = "Número da Carteira Nacional de Habilitação (CNH).",
            example = "01234567890",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String cnh;

    @Column(nullable = false)
    @Schema(description = "CPF do motorista (somente números).",
            example = "99988877766",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String cpf;

    // @Column(nullable = false) // REMOVER ESTA LINHA
    // private String placaVeiculo; // REMOVER ESTA LINHA

    @Column(nullable = false)
    @Schema(description = "Data de validade da CNH.",
            example = "2028-10-20",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate valCnh;

    // --- RELACIONAMENTO ADICIONADO ---
    // Se um motorista pode ter vários veículos associados
    @OneToMany(mappedBy = "motorista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "Lista de veículos associados a este motorista.") // <-- ADICIONEI
    private List<Veiculo> veiculos;

    // Se um motorista só pode ter UM veículo (ajuste conforme sua regra):
    // @OneToOne(mappedBy = "motorista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private Veiculo veiculo;
    // --- FIM DO RELACIONAMENTO ---

}