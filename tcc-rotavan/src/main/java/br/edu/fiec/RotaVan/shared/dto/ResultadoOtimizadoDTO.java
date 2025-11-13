package br.edu.fiec.RotaVan.shared.dto;

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO (Data Transfer Object) para encapsular a resposta
 * da API do Google Maps após otimizar uma rota.
 */
@Data
public class ResultadoOtimizadoDTO {

    private BigDecimal distanciaKm;
    private Integer tempoMin;
    private List<Ponto> pontosOrdenados;

}