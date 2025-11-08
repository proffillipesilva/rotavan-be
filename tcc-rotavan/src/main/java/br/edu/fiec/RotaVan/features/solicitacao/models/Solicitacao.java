package br.edu.fiec.RotaVan.features.solicitacao.models;

import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "solicitacao")
@Data
@Schema(description = "Representa uma solicitação de vaga de um Responsável para um Motorista.")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único da solicitação (UUID).",
            example = "s1a2b3c4-d5e6-f789-0123-456789solic")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dependente") // O "Dependente" do guia é sua Crianca
    @Schema(description = "O dependente (criança) para o qual a vaga está sendo solicitada.")
    private Crianca crianca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista")
    @Schema(description = "O motorista que recebeu a solicitação.")
    private Motoristas motorista;

    @Column(nullable = false)
    @Schema(description = "Status atual da solicitação.",
            example = "PENDENTE",
            allowableValues = {"PENDENTE", "ACEITO", "RECUSADO"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status; // Ex: "PENDENTE", "ACEITO", "RECUSADO"

    // Uma Solicitação pode ter várias rotas (Ida e Volta)
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de rotas (ida e/ou volta) sugeridas para o motorista avaliar.")
    private List<Rota> rotasSugeridas;
}