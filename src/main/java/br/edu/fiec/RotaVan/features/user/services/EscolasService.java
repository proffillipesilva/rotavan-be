package br.edu.fiec.RotaVan.features.user.services;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EscolasService {
    Escolas save(Escolas escola);
    List<Escolas> findAll();
    Optional<Escolas> findById(UUID id);
    Optional<Escolas> update(UUID id, Escolas escolaDetails);
    boolean deleteById(UUID id);
}