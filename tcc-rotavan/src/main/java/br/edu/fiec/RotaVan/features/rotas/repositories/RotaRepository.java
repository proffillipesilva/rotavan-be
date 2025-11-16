package br.edu.fiec.RotaVan.features.rotas.repositories; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RotaRepository extends JpaRepository<Rota, UUID> {
}