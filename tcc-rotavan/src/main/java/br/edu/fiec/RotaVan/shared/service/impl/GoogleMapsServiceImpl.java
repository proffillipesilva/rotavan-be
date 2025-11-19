package br.edu.fiec.RotaVan.shared.service.impl;

import br.edu.fiec.RotaVan.shared.dto.ResultadoOtimizadoDTO;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi; // <--- Importante
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult; // <--- Importante
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

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
        // ... (O seu código de otimização de rota mantém-se igual aqui) ...
        // Para brevidade, não vou repetir o código de otimizarRota, mantenha o que já tem.
        return null; // (Apenas exemplo, mantenha o seu código original deste método)
    }

    // --- AQUI ESTÁ A NOVA LÓGICA ---
    @Override
    public LatLng buscarCoordenadas(String endereco) {
        try {
            // Pede ao Google: "Onde fica este endereço?"
            GeocodingResult[] results = GeocodingApi.newRequest(context)
                    .address(endereco)
                    .await();

            // Se o Google encontrar, devolve a primeira localização (Latitude/Longitude)
            if (results != null && results.length > 0) {
                return results[0].geometry.location;
            }
        } catch (Exception e) {
            // Se der erro, apenas imprimimos no log e devolvemos null
            // Assim o sistema não "crasha", apenas fica sem coordenadas
            e.printStackTrace();
        }
        return null;
    }
}