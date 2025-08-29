package br.edu.fiec.RotaVan.features.user.services.impl;

import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.features.user.services.ResponsaveisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ResponsaveisServiceImpl implements ResponsaveisService, UserDetailsService {


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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) responsaveisRepository.findByEmail(email).orElseThrow();
    }
}