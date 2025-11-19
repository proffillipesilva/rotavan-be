package br.edu.fiec.RotaVan.features.user.repositories;

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

// Correção: JpaRepository<Motoristas, Integer>
@Repository
public interface MotoristasRepository extends JpaRepository<Motoristas, UUID> {
    Optional<Motoristas> findByUser(User user);
}