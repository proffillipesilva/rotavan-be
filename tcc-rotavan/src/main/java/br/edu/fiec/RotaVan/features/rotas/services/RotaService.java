package br.edu.fiec.RotaVan.features.rotas.services; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;

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
}