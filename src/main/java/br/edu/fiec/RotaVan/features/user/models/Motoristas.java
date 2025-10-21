package br.edu.fiec.RotaVan.features.user.models;

// --- IMPORTAÇÕES ADICIONADAS/REVISADAS ---
import br.edu.fiec.RotaVan.features.veiculos.models.Veiculo; // Importar Veiculo
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List; // Importar List
import java.util.UUID;
// --- FIM DAS IMPORTAÇÕES ---

@Entity
// @Table(name = "motoristas") // Opcional
@Data
public class Motoristas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String nomeMotorista;

    @Column(nullable = false)
    private String cnh;

    @Column(nullable = false)
    private String cpf;

    // @Column(nullable = false) // REMOVER ESTA LINHA
    // private String placaVeiculo; // REMOVER ESTA LINHA

    @Column(nullable = false)
    private LocalDate valCnh;

    // --- RELACIONAMENTO ADICIONADO ---
    // Se um motorista pode ter vários veículos associados
    @OneToMany(mappedBy = "motorista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Veiculo> veiculos;

    // Se um motorista só pode ter UM veículo (ajuste conforme sua regra):
    // @OneToOne(mappedBy = "motorista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private Veiculo veiculo;
    // --- FIM DO RELACIONAMENTO ---

}