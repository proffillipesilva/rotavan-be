package br.edu.fiec.RotaVan.features.user.services.impl;

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.features.user.services.MotoristasService;
import jakarta.persistence.EntityNotFoundException;
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
    public Motoristas findById(UUID id) {
        return motoristasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado com ID: " + id));
    }

    @Override
    public Motoristas save(Motoristas motorista) {
        // Este método será ajustado quando criarmos o registo de motoristas
        return motoristasRepository.save(motorista);
    }

    @Override
    public Optional<Motoristas> update(UUID id, Motoristas motoristaDetails) {
        return motoristasRepository.findById(id)
                .map(existingMotorista -> {
                    // ATUALIZAÇÃO AQUI: Removemos as linhas de email e password
                    existingMotorista.setNomeMotorista(motoristaDetails.getNomeMotorista());
                    existingMotorista.setCnh(motoristaDetails.getCnh());
                    existingMotorista.setCpf(motoristaDetails.getCpf());
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