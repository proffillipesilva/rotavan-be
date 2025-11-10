package br.edu.fiec.RotaVan.features.rotas.models; // Ajuste o pacote conforme necessário

import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rotas") // Definindo explicitamente o nome da tabela (opcional, mas bom)
@Data
@Schema(description = "Representa uma Rota (trajeto) de transporte, contendo múltiplos Pontos.")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único da rota (UUID).",
            example = "r1a2b3c4-d5e6-f789-0123-456789rota")
    private UUID id;

    @Column(name = "nome_rota", nullable = false, length = 100)
    @Schema(description = "Nome descritivo da rota.",
            example = "Rota Manhã - Colégio X",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomeRota;

    @Column(length = 255)
    @Schema(description = "Descrição opcional da rota.",
            example = "Rota que passa pelos bairros Jardim Morada do Sol e Centro.")
    private String descricao;

    @Column(name = "distancia_km", precision = 6, scale = 2)
    @Schema(description = "Distância total estimada da rota em quilômetros.",
            example = "15.50")
    private BigDecimal distanciaKm;

    @Column(name = "tempo_estimado_min")
    @Schema(description = "Tempo total estimado da rota em minutos.",
            example = "45")
    private Integer tempoEstimadoMin;

    // Relacionamento com Ponto (Uma Rota tem muitos Pontos)
    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de pontos de parada que compõem a rota.")
    private List<Ponto> pontos;

    // Relacionamento com Viagem (Uma Rota pode estar em muitas Viagens)
    // Usamos FetchType.LAZY para não carregar todas as viagens sempre que carregar uma rota
    @OneToMany(mappedBy = "rota", fetch = FetchType.LAZY)
    @Schema(description = "Histórico de viagens associadas a esta rota (carregado sob demanda).")
    private List<Viagem> viagens;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_solicitacao") // Chave estrangeira
    @JsonIgnore // Evita loops infinitos ao buscar
    @Schema(description = "Solicitação de serviço que originou esta rota (ignorado no JSON).")
    private Solicitacao solicitacao;
}