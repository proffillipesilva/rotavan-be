package br.edu.fiec.RotaVan.features.rotas.services.impl;

import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.repositories.RotaRepository;
import br.edu.fiec.RotaVan.features.rotas.services.RotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RotaServiceImpl implements RotaService {

    private final RotaRepository rotaRepository;

    @Override
    public Rota criarRota(Rota rota) {
        return rotaRepository.save(rota);
    }

    @Override
    public List<Rota> listarRotas() {
        return rotaRepository.findAll();
    }

    @Override
    public Rota buscarRotaPorId(UUID id) {
        return rotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada com ID: " + id));
    }

    @Override
    public Rota atualizarRota(UUID id, Rota rotaDetails) {
        Rota rota = buscarRotaPorId(id);

        // CORREÇÃO: Usar getNome() em vez de getNomeRota()
        rota.setNome(rotaDetails.getNome());
        rota.setDescricao(rotaDetails.getDescricao());
        rota.setDistancia(rotaDetails.getDistancia());
        rota.setTempoEstimado(rotaDetails.getTempoEstimado());

        // Se tivermos o campo 'tipo', atualizamos também
        if (rotaDetails.getTipo() != null) {
            rota.setTipo(rotaDetails.getTipo());
        }

        return rotaRepository.save(rota);
    }

    @Override
    public void deletarRota(UUID id) {
        Rota rota = buscarRotaPorId(id);
        rotaRepository.delete(rota);
    }
}