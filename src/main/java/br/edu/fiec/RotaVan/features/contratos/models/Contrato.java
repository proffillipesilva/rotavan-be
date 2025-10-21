package br.edu.fiec.RotaVan.features.contratos.models; // Exemplo de pacote

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "contratos") // Nome da tabela
@Data
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Column(name = "valor_mensal", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMensal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_responsavel", nullable = false)
    private Responsaveis responsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista", nullable = false)
    private Motoristas motorista;
}