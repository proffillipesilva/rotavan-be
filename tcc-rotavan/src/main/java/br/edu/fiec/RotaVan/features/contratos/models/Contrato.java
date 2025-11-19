package br.edu.fiec.RotaVan.features.contratos.models;

import br.edu.fiec.RotaVan.features.user.models.Crianca; // Importante se tiver vínculo
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID; // UUID

@Entity
@Table(name = "contratos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "valor_mensal")
    private BigDecimal valorMensal;

    private String status; // "ATIVO", "ENCERRADO"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista")
    @JsonIgnore
    private Motoristas motorista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_responsavel")
    @JsonIgnore
    private Responsaveis responsavel;

    // Sugestão: Adicionar vínculo com a Criança também, se fizer sentido para o seu negócio
    // @ManyToOne
    // @JoinColumn(name = "fk_crianca")
    // private Crianca crianca;
}