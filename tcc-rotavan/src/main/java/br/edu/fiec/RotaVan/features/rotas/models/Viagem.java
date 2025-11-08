package br.edu.fiec.RotaVan.features.rotas.models; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.user.models.Crianca; // Mudar de Dependente para Crianca
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "viagens") // Nome da tabela
@Data
@Schema(description = "Representa uma viagem (corrida) individual agendada ou realizada.")
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único da viagem (UUID).",
            example = "v1a2b3c4-d5e6-f789-0123-456789viag")
    private UUID id;

    @Column(nullable = false)
    @Schema(description = "Data em que a viagem ocorre.",
            example = "2025-11-07",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate data; // DATE no SQL mapeia para LocalDate

    @Column(name = "hora_saida", nullable = false)
    @Schema(description = "Horário de início/saída da viagem.",
            example = "07:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalTime horaSaida; // TIME no SQL mapeia para LocalTime

    @Column(name = "hora_chegada")
    @Schema(description = "Horário de término/chegada da viagem.",
            example = "08:15:00")
    private LocalTime horaChegada;

    @Column(length = 50)
    @Schema(description = "Status atual da viagem (ex: AGENDADA, EM_ANDAMENTO, FINALIZADA, CANCELADA).",
            example = "AGENDADA")
    private String status;

    // Ajustar o nome da entidade para Crianca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dependente", nullable = false) // Manter o nome da FK do SQL
    @Schema(description = "O dependente (criança) que está sendo transportado nesta viagem.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Crianca crianca; // Usar a classe Crianca

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista", nullable = false)
    @Schema(description = "O motorista responsável pela viagem.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Motoristas motorista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_rota", nullable = false)
    @Schema(description = "A rota que está sendo executada nesta viagem.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Rota rota;
}