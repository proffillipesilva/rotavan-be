package br.edu.fiec.RotaVan.repositories;

import br.edu.fiec.RotaVan.models.Motoristas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MotoristasRepository extends JpaRepository<Motoristas, UUID> {
    // JpaRepository já fornece métodos como save(), findAll(), findById(), deleteById(), etc.
}