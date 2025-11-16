package br.edu.fiec.RotaVan.shared.service.impl;

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.shared.dto.ResultadoOtimizadoDTO;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j // Para registrar logs de erro
public class GoogleMapsServiceImpl implements GoogleMapsService {

    // 1. Injeta o Bean que criamos na configuração
    private final GeoApiContext geoApiContext;

    @Override
    public ResultadoOtimizadoDTO otimizarRota(String origem, String destino, List<String> waypoints) {
        try {
            // 2. Monta a requisição para a API
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(origem)
                    .destination(destino)
                    .waypoints(waypoints.toArray(new String[0])) // Converte a lista de alunos
                    .optimizeWaypoints(true) // Pede ao Google para otimizar a ordem dos waypoints
                    .await(); // Executa a chamada (síncrona)

            // 3. Processa a resposta
            if (result.routes != null && result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];
                return processarResultado(route, origem, destino, waypoints);
            } else {
                throw new RuntimeException("Nenhuma rota encontrada pela API do Google.");
            }

        } catch (Exception e) {
            log.error("Erro ao chamar API do Google Maps: {}", e.getMessage());
            // Em caso de erro na API, lança uma exceção para parar o processo
            throw new RuntimeException("Falha ao calcular rota: " + e.getMessage(), e);
        }
    }

    /**
     * Método privado para extrair os dados da resposta do Google.
     */
    private ResultadoOtimizadoDTO processarResultado(DirectionsRoute route, String origem, String destino, List<String> waypoints) {
        ResultadoOtimizadoDTO dto = new ResultadoOtimizadoDTO();
        List<Ponto> pontosOrdenados = new ArrayList<>();

        long distanciaTotalMetros = 0;
        long tempoTotalSegundos = 0;

        // 1. O Google otimiza os "waypoints". A ordem real estará em 'waypointOrder'
        // Ex: se waypoints = [A, B, C], a ordem pode ser [1, 0, 2] (B, A, C)
        int[] waypointOrder = route.waypointOrder;

        // --- Montagem da Lista de Pontos na Ordem Correta ---

        // Adiciona a Origem (Motorista ou Escola) - Ordem 1
        pontosOrdenados.add(criarPonto(origem, 1, route.legs[0].startLocation));

        // Adiciona os Waypoints (Alunos) na ordem otimizada
        for (int i = 0; i < waypointOrder.length; i++) {
            int waypointIndex = waypointOrder[i]; // O índice do waypoint original
            String enderecoWaypoint = waypoints.get(waypointIndex);

            // Pega a localização (lat/lng) do final da "perna" anterior da viagem
            LatLng localizacao = route.legs[i].endLocation;

            // A ordem é i + 2 (pois o Ponto 1 foi a origem)
            pontosOrdenados.add(criarPonto(enderecoWaypoint, i + 2, localizacao));
        }

        // Adiciona o Destino (Escola ou Motorista) - Ordem final
        pontosOrdenados.add(criarPonto(destino, pontosOrdenados.size() + 1, route.legs[route.legs.length - 1].endLocation));

        // --- Cálculo de Distância e Tempo Total ---
        // Soma as "pernas" (legs) da rota
        for (DirectionsLeg leg : route.legs) {
            distanciaTotalMetros += leg.distance.inMeters;
            tempoTotalSegundos += leg.duration.inSeconds;
        }

        // Converte para as unidades que queremos salvar no banco
        BigDecimal distanciaKm = BigDecimal.valueOf(distanciaTotalMetros / 1000.0)
                .setScale(2, RoundingMode.HALF_UP);
        Integer tempoMin = (int) Math.ceil(tempoTotalSegundos / 60.0);

        // 4. Popula o DTO e retorna
        dto.setDistanciaKm(distanciaKm);
        dto.setTempoMin(tempoMin);
        dto.setPontosOrdenados(pontosOrdenados);

        return dto;
    }

    /**
     * Método helper para criar um objeto Ponto
     */
    private Ponto criarPonto(String nomeOuEndereco, int ordem, LatLng localizacao) {
        Ponto p = new Ponto();
        p.setNomePonto(nomeOuEndereco); // Salva o endereço/nome
        p.setOrdem(ordem); // Salva a ordem
        // Salva a Lat/Lng exata retornada pelo Google
        p.setLatitude(BigDecimal.valueOf(localizacao.lat).setScale(7, RoundingMode.HALF_UP));
        p.setLongitude(BigDecimal.valueOf(localizacao.lng).setScale(7, RoundingMode.HALF_UP));
        return p;
    }
}