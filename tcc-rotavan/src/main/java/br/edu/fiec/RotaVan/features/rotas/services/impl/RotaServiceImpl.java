package br.edu.fiec.RotaVan.features.rotas.services.impl; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.repositories.PontoRepository; // Importar PontoRepository
import br.edu.fiec.RotaVan.features.rotas.repositories.RotaRepository;
import br.edu.fiec.RotaVan.features.rotas.services.RotaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Value; // <-- 1. IMPORTAÇÃO ADICIONADA

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RotaServiceImpl implements RotaService {

    private final RotaRepository rotaRepository;
    private final PontoRepository pontoRepository;

    // --- CAMPO ADICIONADO (Passo 3) ---
    @Value("${google.maps.api.key}") // <-- 2. INJETA A CHAVE DO PROPERTIES
    private String googleMapsApiKey;
    // --- FIM DA ADIÇÃO ---


    public RotaServiceImpl(RotaRepository rotaRepository, PontoRepository pontoRepository) {
        this.rotaRepository = rotaRepository;
        this.pontoRepository = pontoRepository; // Inicializar
    }

    @Override
    @Transactional // Boa prática para métodos que modificam dados
    public Rota save(Rota rota) {
        // Se a rota já vem com pontos, garantir que a referência da rota está definida neles
        if (rota.getPontos() != null) {
            rota.getPontos().forEach(ponto -> ponto.setRota(rota));
        }
        return rotaRepository.save(rota);
    }

    @Override
    public List<Rota> findAll() {
        return rotaRepository.findAll();
    }

    @Override
    public Optional<Rota> findById(UUID id) {
        return rotaRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Rota> update(UUID id, Rota rotaDetails) {
        return rotaRepository.findById(id)
                .map(existingRota -> {
                    existingRota.setNomeRota(rotaDetails.getNomeRota());
                    existingRota.setDescricao(rotaDetails.getDescricao());
                    existingRota.setDistanciaKm(rotaDetails.getDistanciaKm());
                    existingRota.setTempoEstimadoMin(rotaDetails.getTempoEstimadoMin());
                    // Atualizar pontos pode ser mais complexo, talvez um método separado
                    return rotaRepository.save(existingRota);
                });
    }

    @Override
    @Transactional
    public boolean deleteById(UUID id) {
        if (rotaRepository.existsById(id)) {
            rotaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Optional<Ponto> adicionarPontoNaRota(UUID rotaId, Ponto ponto) {
        return rotaRepository.findById(rotaId).map(rota -> {
            ponto.setRota(rota); // Garante a associação bidirecional
            // Poderia adicionar o ponto à lista rota.getPontos() se ela estivesse carregada (EAGER ou buscada)
            return pontoRepository.save(ponto);
        });
    }

    @Override
    public List<Ponto> findPontosByRotaId(UUID rotaId) {
        // Exemplo simples, poderia usar um método customizado no PontoRepository
        return rotaRepository.findById(rotaId)
                .map(Rota::getPontos) // Retorna a lista de pontos da rota encontrada
                .orElse(Collections.emptyList()); // Retorna lista vazia se a rota não for encontrada
    }

    // --- MÉTODO DE EXEMPLO (Adicionado no Passo 3) ---
    /**
     * Exemplo de método que usaria a chave de API para o geocoding.
     * @param endereco O endereço a ser convertido em coordenadas.
     */
    public void fazerGeocoding(String endereco) {
        // A tua chave de API está pronta a ser usada através da variável:
        System.out.println("A API Key do Google Maps é: " + this.googleMapsApiKey);

        // AQUI entraria a lógica da biblioteca de Geocoding
        // (Ex: com.google.maps.GeoApiContext)
    }
}