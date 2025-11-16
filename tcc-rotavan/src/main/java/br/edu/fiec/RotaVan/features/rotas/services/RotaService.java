package br.edu.fiec.RotaVan.features.rotas.services; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import com.google.api.client.util.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RotaService {
    Rota save(Rota rota);
    List<Rota> findAll();
    Optional<Rota> findById(UUID id);
    Optional<Rota> update(UUID id, Rota rotaDetails);
    boolean deleteById(UUID id);
    // Métodos específicos, por exemplo:
    Optional<Ponto> adicionarPontoNaRota(UUID rotaId, Ponto ponto);
    List<Ponto> findPontosByRotaId(UUID rotaId);

    @Service
    public class GeocodingService {

        @Autowired
        private RestTemplate restTemplate;

        @Value("${google.maps.api.key}")
        private String apiKey;

        // URL para geocodificação direta (forward)
        private static final String GOOGLE_API_URL =
                "https://maps.googleapis.com/maps/api/geocode/json?address={address}&key={key}";

        /**
         * Converte um endereço em coordenadas.
         */
        public String getCoordinatesFromAddress(String address) {

            // Codifica o endereço para ser seguro para URL (ex: trocar espaços por "+")
            // (O RestTemplate geralmente lida com isso se usado como variável URI)

            java.util.Map<String, String> params = new java.util.HashMap<>();
            params.put("address", address);
            params.put("key", apiKey);

            // Chama a API
            String jsonResponse = restTemplate.getForObject(GOOGLE_API_URL, String.class, params);

            // Aqui você processaria o JSON para extrair "geometry.location.lat" e "lng"

            return jsonResponse;
        }
    }
}