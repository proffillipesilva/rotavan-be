package br.edu.fiec.RotaVan.shared.service;

import br.edu.fiec.RotaVan.shared.dto.ResultadoOtimizadoDTO;
import com.google.maps.model.LatLng; // <--- Importante: Adicionar esta linha

import java.util.List;

public interface GoogleMapsService {

    ResultadoOtimizadoDTO otimizarRota(String origem, String destino, List<String> paradas);

    // --- NOVO MÉTODO ---
    LatLng buscarCoordenadas(String endereco);
}