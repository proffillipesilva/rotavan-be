package br.edu.fiec.RotaVan.features.veiculos.repositories; // Ajuste o pacote se necessário

import br.edu.fiec.RotaVan.features.veiculos.models.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {
    // Exemplo de método personalizado:
    Optional<Veiculo> findByPlaca(String placa);
}