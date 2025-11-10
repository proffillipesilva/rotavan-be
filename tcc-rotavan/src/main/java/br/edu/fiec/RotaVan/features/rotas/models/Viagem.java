package br.edu.fiec.RotaVan.features.rotas.models;

import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
// import java.util.Date; // <-- Removido
import java.util.UUID;

@Entity
@Table(name = "viagens")
@Data
public class Viagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // --- CORREÇÃO AQUI ---
    @Column(name = "data_viagem", nullable = false) // <-- Mapeando para a coluna correta
    private LocalDate data; // DATE no SQL mapeia para LocalDate
    // --- FIM DA CORREÇÃO ---

    @Column(name = "hora_saida", nullable = false)
    private LocalTime horaSaida;

    @Column(name = "hora_chegada")
    private LocalTime horaChegada;

    @Column(length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dependente", nullable = false)
    private Crianca crianca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista", nullable = false)
    private Motoristas motorista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_rota", nullable = false)
    private Rota rota;
}