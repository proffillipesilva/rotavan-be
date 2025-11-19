package br.edu.fiec.RotaVan.features.rotas.services;

import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import java.util.List;
import java.util.UUID;

public interface RotaService {
    Rota criarRota(Rota rota);
    List<Rota> listarRotas();
    Rota buscarRotaPorId(UUID id);
    Rota atualizarRota(UUID id, Rota rota);
    void deletarRota(UUID id);
}