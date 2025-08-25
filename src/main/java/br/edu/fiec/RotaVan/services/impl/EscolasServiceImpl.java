package br.edu.fiec.RotaVan.services;

import br.edu.fiec.RotaVan.models.Escolas;
import br.edu.fiec.RotaVan.repositories.EscolasRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EscolasServiceImpl implements EscolasService {

    private final EscolasRepository escolasRepository;

    public EscolasServiceImpl(EscolasRepository escolasRepository) {
        this.escolasRepository = escolasRepository;
    }

    @Override
    public Escolas save(Escolas escola) {
        return escolasRepository.save(escola);
    }

    @Override
    public List<Escolas> findAll() {
        return escolasRepository.findAll();
    }

    @Override
    public Optional<Escolas> findById(UUID id) {
        return escolasRepository.findById(id);
    }

    @Override
    public Optional<Escolas> update(UUID id, Escolas escolaDetails) {
        return escolasRepository.findById(id)
                .map(existingEscola -> {
                    existingEscola.setNome(escolaDetails.getNome());
                    existingEscola.setCnpj(escolaDetails.getCnpj());
                    existingEscola.setEndereco(escolaDetails.getEndereco());
                    existingEscola.setTelefone(escolaDetails.getTelefone());
                    return escolasRepository.save(existingEscola);
                });
    }

    @Override
    public boolean deleteById(UUID id) {
        if (escolasRepository.existsById(id)) {
            escolasRepository.deleteById(id);
            return true;
        }
        return false;
    }
}