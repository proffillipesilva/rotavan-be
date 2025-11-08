package br.edu.fiec.RotaVan.features.contratos.models; // Exemplo de pacote

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "contratos") // Nome da tabela
@Data
@Schema(description = "Representa o contrato de serviço entre um Responsável e um Motorista.")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do contrato (UUID).",
            example = "c1a2b3c4-d5e6-f789-0123-456789contr")
    private UUID id;

    @Column(name = "data_inicio", nullable = false)
    @Schema(description = "Data de início da vigência do contrato.",
            example = "2025-02-01",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    @Schema(description = "Data de término da vigência do contrato.",
            example = "2025-12-31",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dataFim;

    @Column(name = "valor_mensal", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Valor da mensalidade acordado.",
            example = "450.50",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal valorMensal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_responsavel", nullable = false)
    @Schema(description = "O responsável (cliente) que está contratando o serviço.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Responsaveis responsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista", nullable = false)
    @Schema(description = "O motorista (prestador) que está sendo contratado.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Motoristas motorista;
}