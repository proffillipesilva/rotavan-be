package br.edu.fiec.RotaVan.features.rotas.services.impl; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.repositories.PontoRepository; // Importar PontoRepository
import br.edu.fiec.RotaVan.features.rotas.repositories.RotaRepository;
import br.edu.fiec.RotaVan.features.rotas.services.RotaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RotaServiceImpl implements RotaService {

    private final RotaRepository rotaRepository;
    private final PontoRepository pontoRepository; // Injetar PontoRepository

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
}