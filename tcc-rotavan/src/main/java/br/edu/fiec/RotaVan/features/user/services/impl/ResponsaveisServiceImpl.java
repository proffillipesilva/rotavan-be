package br.edu.fiec.RotaVan.features.user.services.impl;

import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.repositories.CriancaRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.features.user.services.ResponsaveisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResponsaveisServiceImpl implements ResponsaveisService {

    private final ResponsaveisRepository responsaveisRepository;
    private final CriancaRepository criancaRepository;

    public ResponsaveisServiceImpl(ResponsaveisRepository responsaveisRepository, CriancaRepository criancaRepository) {
        this.responsaveisRepository = responsaveisRepository;
        this.criancaRepository = criancaRepository;
    }

    @Override
    @Transactional
    public Responsaveis criaResponsavel(Responsaveis responsavel) {
        if (responsavel.getCriancas() != null) {
            for (Crianca crianca : responsavel.getCriancas()) {
                crianca.setResponsavel(responsavel);
            }
        }
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

    @Override
    @Transactional
    public Optional<Crianca> adicionarCrianca(UUID responsavelId, Crianca novaCrianca) {
        return responsaveisRepository.findById(responsavelId).map(responsavel -> {
            novaCrianca.setResponsavel(responsavel);
            return criancaRepository.save(novaCrianca);
        });
    }

    @Override
    @Transactional
    public Optional<Responsaveis> update(UUID id, Responsaveis responsavelDetails) {
        return responsaveisRepository.findById(id)
                .map(existingResponsavel -> {
                    existingResponsavel.setNomeResponsavel(responsavelDetails.getNomeResponsavel());
                    existingResponsavel.setCpfResponsavel(responsavelDetails.getCpfResponsavel());
                    existingResponsavel.setEnderecoCasa(responsavelDetails.getEnderecoCasa());
                    return responsaveisRepository.save(existingResponsavel);
                });
    }

}