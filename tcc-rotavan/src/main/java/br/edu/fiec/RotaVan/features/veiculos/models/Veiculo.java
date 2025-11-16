package br.edu.fiec.RotaVan.features.veiculos.models; // Exemplo de pacote diferente

import br.edu.fiec.RotaVan.features.user.models.Motoristas; // Importar Motoristas
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "veiculos") // Nome da tabela
@Data
@Schema(description = "Representa um Veículo (van) associado a um motorista.")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do veículo (UUID).",
            example = "v1a2b3c4-d5e6-f789-0123-456789veic")
    private UUID id;

    @Column(nullable = false, unique = true, length = 10)
    @Schema(description = "Placa do veículo (formato Mercosul ou antigo).",
            example = "BRA2E19",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String placa;

    @Column(nullable = false)
    @Schema(description = "Número de assentos (capacidade) do veículo.",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer capacidade;

    @Column(length = 50)
    @Schema(description = "Modelo do veículo.",
            example = "Mercedes-Benz Sprinter")
    private String modelo;

    // Relacionamento com Motorista (Muitos Veículos podem pertencer a um Motorista?)
    // Ou Um Veículo pertence a Um Motorista? -> Pelo SQL parece ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista", nullable = false) // Nome da coluna FK
    @Schema(description = "O motorista proprietário ou associado a este veículo.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Motoristas motorista;
}