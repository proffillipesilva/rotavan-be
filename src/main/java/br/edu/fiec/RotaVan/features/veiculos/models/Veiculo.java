package br.edu.fiec.RotaVan.features.veiculos.models; // Exemplo de pacote diferente

import br.edu.fiec.RotaVan.features.user.models.Motoristas; // Importar Motoristas
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "veiculos") // Nome da tabela
@Data
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(length = 50)
    private String modelo;

    // Relacionamento com Motorista (Muitos Veículos podem pertencer a um Motorista?)
    // Ou Um Veículo pertence a Um Motorista? -> Pelo SQL parece ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista", nullable = false) // Nome da coluna FK
    private Motoristas motorista;
}