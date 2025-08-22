package br.edu.fiec.RotaVan.repositories;

import br.edu.fiec.RotaVan.models.Responsaveis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResponsaveisRepository extends JpaRepository<Responsaveis, UUID> {
}
