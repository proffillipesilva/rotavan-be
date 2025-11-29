package br.edu.fiec.RotaVan.features.user.models;

import br.edu.fiec.RotaVan.features.veiculos.models.Veiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Schema(description = "Representa os dados específicos de um Motorista.")
public class Motoristas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private String nomeMotorista;

    @Column(nullable = false)
    private String cnh;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDate valCnh;

    @OneToMany(mappedBy = "motorista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Veiculo> veiculos;
}