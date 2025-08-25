package br.edu.fiec.RotaVan.repositories;

import br.edu.fiec.RotaVan.models.Escolas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EscolasRepository extends JpaRepository<Escolas, UUID> {

}
