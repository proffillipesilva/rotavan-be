package br.edu.fiec.RotaVan.features.veiculos.services.impl; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.veiculos.models.Veiculo;
import br.edu.fiec.RotaVan.features.veiculos.repositories.VeiculoRepository;
import br.edu.fiec.RotaVan.features.veiculos.services.VeiculoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoServiceImpl(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    @Transactional
    public Veiculo save(Veiculo veiculo) {
        // Validação de Placa (exemplo)
        veiculoRepository.findByPlaca(veiculo.getPlaca()).ifPresent(v -> {
            throw new IllegalArgumentException("Placa já cadastrada: " + veiculo.getPlaca());
        });
        return veiculoRepository.save(veiculo);
    }

    @Override
    public List<Veiculo> findAll() {
        return veiculoRepository.findAll();
    }

    @Override
    public Optional<Veiculo> findById(UUID id) {
        return veiculoRepository.findById(id);
    }

    @Override
    public Optional<Veiculo> findByPlaca(String placa) {
        // Este método pode ser gerado pelo Spring Data JPA no VeiculoRepository
        return veiculoRepository.findByPlaca(placa);
    }

    @Override
    public List<Veiculo> findByMotorista(Motoristas motorista) {
        // Idealmente, crie um método no VeiculoRepository: List<Veiculo> findByMotorista(Motoristas motorista);
        return veiculoRepository.findAll().stream()
                .filter(v -> v.getMotorista() != null && v.getMotorista().getId().equals(motorista.getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Veiculo> update(UUID id, Veiculo veiculoDetails) {
        return veiculoRepository.findById(id)
                .map(existingVeiculo -> {
                    // Verifica se a placa está sendo alterada para uma já existente (exceto a do próprio veículo)
                    if (!existingVeiculo.getPlaca().equalsIgnoreCase(veiculoDetails.getPlaca())) {
                        veiculoRepository.findByPlaca(veiculoDetails.getPlaca()).ifPresent(v -> {
                            throw new IllegalArgumentException("Placa já cadastrada: " + veiculoDetails.getPlaca());
                        });
                        existingVeiculo.setPlaca(veiculoDetails.getPlaca());
                    }
                    existingVeiculo.setCapacidade(veiculoDetails.getCapacidade());
                    existingVeiculo.setModelo(veiculoDetails.getModelo());
                    existingVeiculo.setMotorista(veiculoDetails.getMotorista()); // Permite trocar o motorista associado
                    return veiculoRepository.save(existingVeiculo);
                });
    }

    @Override
    @Transactional
    public boolean deleteById(UUID id) {
        if (veiculoRepository.existsById(id)) {
            veiculoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}