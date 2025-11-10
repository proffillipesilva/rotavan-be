package br.edu.fiec.RotaVan.features.rotas.models; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.user.models.Crianca; // Mudar de Dependente para Crianca
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "viagens") // Nome da tabela
@Data
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate data; // DATE no SQL mapeia para LocalDate

    @Column(name = "hora_saida", nullable = false)
    private LocalTime horaSaida; // TIME no SQL mapeia para LocalTime

    @Column(name = "hora_chegada")
    private LocalTime horaChegada;

    @Column(length = 50)
    private String status;

    // Ajustar o nome da entidade para Crianca
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dependente", nullable = false) // Manter o nome da FK do SQL
    private Crianca crianca; // Usar a classe Crianca

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista", nullable = false)
    private Motoristas motorista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_rota", nullable = false)
    private Rota rota;
}