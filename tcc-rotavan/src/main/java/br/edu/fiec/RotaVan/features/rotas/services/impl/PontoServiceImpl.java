package br.edu.fiec.RotaVan.features.rotas.services.impl; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.repositories.PontoRepository;
import br.edu.fiec.RotaVan.features.rotas.services.PontoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Comparator; // Para ordenar
import java.util.stream.Collectors; // Para ordenar

@Service
public class PontoServiceImpl implements PontoService {

    private final PontoRepository pontoRepository;

    public PontoServiceImpl(PontoRepository pontoRepository) {
        this.pontoRepository = pontoRepository;
    }

    @Override
    @Transactional
    public Ponto save(Ponto ponto) {
        // Poderia adicionar validações aqui, ex: verificar se a rota existe
        return pontoRepository.save(ponto);
    }

    @Override
    public Optional<Ponto> findById(UUID id) {
        return pontoRepository.findById(id);
    }

    @Override
    public List<Ponto> findByRota(Rota rota) {
        // O Spring Data JPA pode gerar este método se definido na interface PontoRepository
        // return pontoRepository.findByRota(rota);
        // Ou implementar manualmente se necessário
        return pontoRepository.findAll().stream()
                .filter(p -> p.getRota() != null && p.getRota().getId().equals(rota.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Ponto> findByRotaIdOrderByOrdemAsc(UUID rotaId) {
        // Exemplo buscando todos e filtrando/ordenando na memória.
        // Idealmente, crie um método no PontoRepository: List<Ponto> findByRotaIdOrderByOrdemAsc(UUID rotaId);
        return pontoRepository.findAll().stream()
                .filter(p -> p.getRota() != null && p.getRota().getId().equals(rotaId))
                .sorted(Comparator.comparing(Ponto::getOrdem))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Ponto> update(UUID id, Ponto pontoDetails) {
        return pontoRepository.findById(id)
                .map(existingPonto -> {
                    existingPonto.setLongitude(pontoDetails.getLongitude());
                    existingPonto.setLatitude(pontoDetails.getLatitude());
                    existingPonto.setNomePonto(pontoDetails.getNomePonto());
                    existingPonto.setOrdem(pontoDetails.getOrdem());
                    // Não alteramos a rota aqui geralmente, isso seria outra operação
                    return pontoRepository.save(existingPonto);
                });
    }

    @Override
    @Transactional
    public boolean deleteById(UUID id) {
        if (pontoRepository.existsById(id)) {
            pontoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}