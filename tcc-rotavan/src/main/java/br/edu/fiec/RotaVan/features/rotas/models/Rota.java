package br.edu.fiec.RotaVan.features.rotas.models; // Ajuste o pacote conforme necessário

import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rotas") // Definindo explicitamente o nome da tabela (opcional, mas bom)
@Data
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome_rota", nullable = false, length = 100)
    private String nomeRota;

    @Column(length = 255)
    private String descricao;

    @Column(name = "distancia_km", precision = 6, scale = 2)
    private BigDecimal distanciaKm;

    @Column(name = "tempo_estimado_min")
    private Integer tempoEstimadoMin;

    // Relacionamento com Ponto (Uma Rota tem muitos Pontos)
    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ponto> pontos;

    // Relacionamento com Viagem (Uma Rota pode estar em muitas Viagens)
    // Usamos FetchType.LAZY para não carregar todas as viagens sempre que carregar uma rota
    @OneToMany(mappedBy = "rota", fetch = FetchType.LAZY)
    private List<Viagem> viagens;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_solicitacao") // Chave estrangeira
    @JsonIgnore // Evita loops infinitos ao buscar
    private Solicitacao solicitacao;
}