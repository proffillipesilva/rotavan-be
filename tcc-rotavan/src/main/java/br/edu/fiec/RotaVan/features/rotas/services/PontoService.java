package br.edu.fiec.RotaVan.features.rotas.services;

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import java.util.List;
import java.util.UUID;

public interface PontoService {
    Ponto criarPonto(Ponto ponto);
    List<Ponto> listarPontos();
    Ponto buscarPontoPorId(UUID id);
    Ponto atualizarPonto(UUID id, Ponto ponto);
    void deletarPonto(UUID id);
}