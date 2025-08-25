package br.edu.fiec.RotaVan.services.impl;

import br.edu.fiec.RotaVan.models.Responsaveis;
import br.edu.fiec.RotaVan.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.services.ResponsaveisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ResponsaveisServiceImpl implements ResponsaveisService {


    private final ResponsaveisRepository responsaveisRepository;

    @Autowired
    public ResponsaveisServiceImpl(ResponsaveisRepository responsaveisRepository) {
        this.responsaveisRepository = responsaveisRepository;
    }

    @Override
    public Responsaveis criaResponsavel(Responsaveis responsavel) {
        return responsaveisRepository.save(responsavel);
    }

    @Override
    public List<Responsaveis> findAll() {
        return responsaveisRepository.findAll();
    }

    @Override
    public Optional<Responsaveis> findById(UUID id) {
        return responsaveisRepository.findById(id);
    }
}