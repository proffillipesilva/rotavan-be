package br.edu.fiec.RotaVan.features.user.services;

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MotoristasService {
    List<Motoristas> findAll();
    Motoristas findById(UUID id);
    Motoristas save(Motoristas motorista);
    Optional<Motoristas> update(UUID id, Motoristas motoristaDetails);
    boolean deleteById(UUID id);
}