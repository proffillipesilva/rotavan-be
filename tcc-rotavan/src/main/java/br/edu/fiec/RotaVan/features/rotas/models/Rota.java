package br.edu.fiec.RotaVan.features.rotas.models;

import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao; // Importar Solicitação
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa uma rota de transporte (pode ser uma sugestão ou uma rota oficial).")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único da rota (UUID).")
    private UUID id;

    @Column(name = "nome_rota")
    @Schema(description = "Nome da rota.", example = "Rota Manhã - Zona Norte")
    private String nome;

    @Schema(description = "Descrição da rota.", example = "Pega alunos do bairro X e leva para escola Y")
    private String descricao;

    @Column(name = "distancia_km")
    private BigDecimal distancia;

    @Column(name = "tempo_estimado_min")
    private Integer tempoEstimado;

    // --- NOVOS CAMPOS NECESSÁRIOS ---

    @Column(length = 50)
    @Schema(description = "Tipo da rota: 'SUGESTAO' (gerada pelo sistema) ou 'OFICIAL' (aceita pelo motorista).")
    private String tipo; // "SUGESTAO", "OFICIAL"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_solicitacao") // Liga a rota à solicitação que a gerou
    @JsonIgnore
    private Solicitacao solicitacao;

    // --------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id")
    @JsonIgnore
    private Motoristas motorista;

    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ponto> pontos;
}