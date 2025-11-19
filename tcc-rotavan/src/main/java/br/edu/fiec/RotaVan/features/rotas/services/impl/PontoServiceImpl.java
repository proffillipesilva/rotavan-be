package br.edu.fiec.RotaVan.features.rotas.services.impl;

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.repositories.PontoRepository;
import br.edu.fiec.RotaVan.features.rotas.services.PontoService;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PontoServiceImpl implements PontoService {

    private final PontoRepository pontoRepository;
    private final GoogleMapsService googleMapsService;

    public PontoServiceImpl(PontoRepository pontoRepository, GoogleMapsService googleMapsService) {
        this.pontoRepository = pontoRepository;
        this.googleMapsService = googleMapsService;
    }

    @Override
    public Ponto criarPonto(Ponto ponto) {
        preencherCoordenadasSeNecessario(ponto);
        return pontoRepository.save(ponto);
    }

    @Override
    public List<Ponto> listarPontos() {
        return pontoRepository.findAll();
    }

    @Override
    public Ponto buscarPontoPorId(UUID id) {
        return pontoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ponto não encontrado"));
    }

    @Override
    public Ponto atualizarPonto(UUID id, Ponto pontoAtualizado) {
        Ponto pontoExistente = buscarPontoPorId(id);
        pontoExistente.setNome(pontoAtualizado.getNome());
        pontoExistente.setEndereco(pontoAtualizado.getEndereco());

        if (pontoAtualizado.getLatitude() != null && pontoAtualizado.getLongitude() != null) {
            pontoExistente.setLatitude(pontoAtualizado.getLatitude());
            pontoExistente.setLongitude(pontoAtualizado.getLongitude());
        } else {
            preencherCoordenadasSeNecessario(pontoExistente);
        }
        return pontoRepository.save(pontoExistente);
    }

    @Override
    public void deletarPonto(UUID id) {
        Ponto ponto = buscarPontoPorId(id);
        pontoRepository.delete(ponto);
    }

    private void preencherCoordenadasSeNecessario(Ponto ponto) {
        if (ponto.getEndereco() != null && !ponto.getEndereco().isEmpty()) {
            boolean semLat = ponto.getLatitude() == null || ponto.getLatitude() == 0.0;
            boolean semLng = ponto.getLongitude() == null || ponto.getLongitude() == 0.0;

            if (semLat || semLng) {
                try {
                    LatLng coords = googleMapsService.buscarCoordenadas(ponto.getEndereco());
                    if (coords != null) {
                        ponto.setLatitude(coords.lat);
                        ponto.setLongitude(coords.lng);
                    }
                } catch (Exception e) {
                    System.err.println("Erro Geocoding: " + e.getMessage());
                }
            }
        }
    }
}