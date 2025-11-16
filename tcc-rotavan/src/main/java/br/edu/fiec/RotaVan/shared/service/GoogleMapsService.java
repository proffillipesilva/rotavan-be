package br.edu.fiec.RotaVan.shared.service;

import br.edu.fiec.RotaVan.shared.dto.ResultadoOtimizadoDTO;
import java.util.List;

/**
 * Interface para o serviço de otimização de rotas.
 * Abstrai a chamada à API externa (Google Maps).
 */
public interface GoogleMapsService {

    /**
     * Otimiza uma rota com base em origem, destino e pontos de parada (waypoints).
     *
     * @param origem O ponto de partida (ex: Endereço do Motorista).
     * @param destino O ponto final (ex: Endereço da Escola).
     * @param waypoints Uma lista de endereços dos alunos.
     * @return Um DTO com os dados da rota otimizada (distância, tempo, pontos na ordem correta).
     */
    ResultadoOtimizadoDTO otimizarRota(String origem, String destino, List<String> waypoints);
}