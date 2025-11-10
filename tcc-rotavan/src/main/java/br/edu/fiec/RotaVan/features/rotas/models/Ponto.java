package br.edu.fiec.RotaVan.features.rotas.models; // Ajuste o pacote conforme necessário

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pontos") // Nome da tabela
@Data
@Schema(description = "Representa um Ponto de parada (geolocalizado) dentro de uma Rota.")
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do ponto (UUID).",
            example = "p1a2b3c4-d5e6-f789-0123-456789ponto")
    private UUID id;

    // Usar BigDecimal para precisão decimal
    @Column(nullable = false, precision = 10, scale = 6)
    @Schema(description = "Coordenada de Longitude do ponto.",
            example = "-47.200000",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal longitude;

    @Column(nullable = false, precision = 10, scale = 6)
    @Schema(description = "Coordenada de Latitude do ponto.",
            example = "-23.100000",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal latitude;

    @Column(name = "nome_ponto", length = 100) // Nome da coluna como no SQL
    @Schema(description = "Nome descritivo do ponto (ex: 'Casa do Enzo', 'Escola X').",
            example = "Casa do Enzo")
    private String nomePonto; // Nome do campo em Java pode ser diferente

    @Column(nullable = false)
    @Schema(description = "Ordem numérica do ponto dentro da rota (0, 1, 2...).",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer ordem;

    // Relacionamento com Rota (Muitos Pontos pertencem a uma Rota)
    @ManyToOne(fetch = FetchType.LAZY) // LAZY é geralmente preferível para ManyToOne
    @JoinColumn(name = "fk_rota", nullable = false) // Nome da coluna FK como no SQL
    @Schema(description = "A rota à qual este ponto pertence.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Rota rota;
}