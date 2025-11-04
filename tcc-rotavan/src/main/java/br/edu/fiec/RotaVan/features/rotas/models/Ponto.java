package br.edu.fiec.RotaVan.features.rotas.models; // Ajuste o pacote conforme necessário

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pontos") // Nome da tabela
@Data
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Usar BigDecimal para precisão decimal
    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "nome_ponto", length = 100) // Nome da coluna como no SQL
    private String nomePonto; // Nome do campo em Java pode ser diferente

    @Column(nullable = false)
    private Integer ordem;

    // Relacionamento com Rota (Muitos Pontos pertencem a uma Rota)
    @ManyToOne(fetch = FetchType.LAZY) // LAZY é geralmente preferível para ManyToOne
    @JoinColumn(name = "fk_rota", nullable = false) // Nome da coluna FK como no SQL
    private Rota rota;
}