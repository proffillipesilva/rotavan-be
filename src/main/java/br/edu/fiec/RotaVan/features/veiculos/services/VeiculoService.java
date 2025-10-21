package br.edu.fiec.RotaVan.features.veiculos.services; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.veiculos.models.Veiculo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeiculoService {
    Veiculo save(Veiculo veiculo);
    List<Veiculo> findAll();
    Optional<Veiculo> findById(UUID id);
    Optional<Veiculo> findByPlaca(String placa);
    List<Veiculo> findByMotorista(Motoristas motorista);
    Optional<Veiculo> update(UUID id, Veiculo veiculoDetails);
    boolean deleteById(UUID id);
}