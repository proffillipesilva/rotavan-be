package br.edu.fiec.RotaVan.services.impl;

import br.edu.fiec.RotaVan.models.Motoristas;
import br.edu.fiec.RotaVan.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.services.MotoristasService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MotoristasServiceImpl implements MotoristasService {

    private final MotoristasRepository motoristasRepository;

    public MotoristasServiceImpl(MotoristasRepository motoristasRepository) {
        this.motoristasRepository = motoristasRepository;
    }

    @Override
    public List<Motoristas> findAll() {
        return motoristasRepository.findAll();
    }

    @Override
    public Optional<Motoristas> findById(UUID id) {
        return motoristasRepository.findById(id);
    }

    @Override
    public Motoristas save(Motoristas motorista) {
        return motoristasRepository.save(motorista);
    }

    @Override
    public Optional<Motoristas> update(UUID id, Motoristas motoristaDetails) {
        return motoristasRepository.findById(id)
                .map(existingMotorista -> {
                    existingMotorista.setEmail(motoristaDetails.getEmail());
                    existingMotorista.setPassword(motoristaDetails.getPassword()); // Atenção com a senha!
                    existingMotorista.setNomeMotorista(motoristaDetails.getNomeMotorista());
                    existingMotorista.setCnh(motoristaDetails.getCnh());
                    existingMotorista.setCpf(motoristaDetails.getCpf());
                    existingMotorista.setPlacaVeiculo(motoristaDetails.getPlacaVeiculo());
                    return motoristasRepository.save(existingMotorista);
                });
    }

    @Override
    public boolean deleteById(UUID id) {
        if (motoristasRepository.existsById(id)) {
            motoristasRepository.deleteById(id);
            return true;
        }
        return false;
    }
}