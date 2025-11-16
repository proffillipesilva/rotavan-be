package br.edu.fiec.RotaVan.features.rotas.services; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PontoService {
    Ponto save(Ponto ponto);
    Optional<Ponto> findById(UUID id);
    List<Ponto> findByRota(Rota rota); // Exemplo de busca específica
    List<Ponto> findByRotaIdOrderByOrdemAsc(UUID rotaId); // Buscar pontos de uma rota ordenados
    Optional<Ponto> update(UUID id, Ponto pontoDetails);
    boolean deleteById(UUID id);
}