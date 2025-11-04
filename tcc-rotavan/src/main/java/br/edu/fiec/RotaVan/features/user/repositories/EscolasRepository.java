package br.edu.fiec.RotaVan.features.user.repositories;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EscolasRepository extends JpaRepository<Escolas, UUID> {

}
