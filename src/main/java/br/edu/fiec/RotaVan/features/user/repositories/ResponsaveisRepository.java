package br.edu.fiec.RotaVan.features.user.repositories;

import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ResponsaveisRepository extends JpaRepository<Responsaveis, UUID> {
}