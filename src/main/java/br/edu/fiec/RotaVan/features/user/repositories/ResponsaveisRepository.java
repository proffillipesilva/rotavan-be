package br.edu.fiec.RotaVan.features.user.repositories;

import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResponsaveisRepository extends JpaRepository<Responsaveis, UUID> {
    Optional<Responsaveis> findByEmail(String email);
}