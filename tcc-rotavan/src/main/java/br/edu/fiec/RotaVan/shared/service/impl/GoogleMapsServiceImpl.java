package br.edu.fiec.RotaVan.shared.service.impl;

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.shared.dto.ResultadoOtimizadoDTO;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.*; // Importa todas as models do Google (LatLng, TravelMode, etc)
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleMapsServiceImpl implements GoogleMapsService {

    private final GeoApiContext context;

    public GoogleMapsServiceImpl(GeoApiContext context) {
        this.context = context;
    }

    @Override
    public ResultadoOtimizadoDTO otimizarRota(String origem, String destino, List<String> paradas) {
        try {
            // 1. Prepara os endereços intermediários (waypoints)
            String[] waypoints = paradas.toArray(new String[0]);

            // 2. Chama a API do Google Directions
            DirectionsResult result = DirectionsApi.newRequest(context)
                    .origin(origem)
                    .destination(destino)
                    .waypoints(waypoints)
                    .optimizeWaypoints(true) // <--- O PULO DO GATO: Otimiza a ordem
                    .mode(TravelMode.DRIVING)
                    .await();

            if (result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];
                ResultadoOtimizadoDTO dto = new ResultadoOtimizadoDTO();
                List<Ponto> pontosOrdenados = new ArrayList<>();

                // 3. Calcula Distância e Tempo Totais da rota otimizada
                long totalDistanceMeters = 0;
                long totalDurationSeconds = 0;

                for (DirectionsLeg leg : route.legs) {
                    totalDistanceMeters += leg.distance.inMeters;
                    totalDurationSeconds += leg.duration.inSeconds;
                }

                // Converte para Km e Minutos
                dto.setDistanciaKm(BigDecimal.valueOf(totalDistanceMeters).divide(BigDecimal.valueOf(1000)));
                dto.setTempoMin((int) (totalDurationSeconds / 60));

                // 4. Reconstrói a lista de Pontos na ORDEM CERTA
                // 'waypointOrder' é um array com os índices na nova ordem (ex: [2, 0, 1])
                int[] ordemOtimizada = route.waypointOrder;

                for (int i = 0; i < ordemOtimizada.length; i++) {
                    int indexOriginal = ordemOtimizada[i];

                    Ponto ponto = new Ponto();
                    // Define a ordem sequencial (1, 2, 3...)
                    ponto.setOrdem(i + 1);

                    // Recupera o endereço original que foi enviado
                    ponto.setNome("Parada " + (i + 1));
                    ponto.setEndereco(paradas.get(indexOriginal));

                    // (Opcional) Pega a Lat/Long exata que o Google usou para este ponto
                    // A perna 'i' sai da origem (ou ponto anterior) e chega neste ponto
                    if (i < route.legs.length) {
                        // AVISO: A API retorna Legs = Waypoints + 1 (última perna é até o destino)
                        // O 'endLocation' da perna 0 é o primeiro ponto de parada.
                        LatLng location = route.legs[i].endLocation;
                        ponto.setLatitude(location.lat);
                        ponto.setLongitude(location.lng);

                        // Podemos usar o endereço formatado oficial do Google
                        ponto.setEndereco(route.legs[i].endAddress);
                    }

                    pontosOrdenados.add(ponto);
                }

                dto.setPontosOrdenados(pontosOrdenados);
                return dto;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Em produção, use um logger: log.error("Erro ao otimizar rota", e);
        }
        return null;
    }

    @Override
    public LatLng buscarCoordenadas(String endereco) {
        try {
            GeocodingResult[] results = GeocodingApi.newRequest(context)
                    .address(endereco)
                    .await();

            if (results != null && results.length > 0) {
                return results[0].geometry.location;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}