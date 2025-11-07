package br.edu.fiec.RotaVan.features.solicitacao.models;

import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "solicitacao")
@Data
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dependente") // O "Dependente" do guia é sua Crianca
    private Crianca crianca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_motorista")
    private Motoristas motorista;

    @Column(nullable = false)
    private String status; // Ex: "PENDENTE", "ACEITO", "RECUSADO"

    // Uma Solicitação pode ter várias rotas (Ida e Volta)
    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rota> rotasSugeridas;
}