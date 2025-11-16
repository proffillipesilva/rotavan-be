package br.edu.fiec.RotaVan.features.user.services;

import br.edu.fiec.RotaVan.features.user.dto.CriancaDTO;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ResponsaveisService {


    Responsaveis criaResponsavel(Responsaveis responsavel);


    List<Responsaveis> findAll();


    Optional<Responsaveis> findById(UUID id);

    // Em ResponsaveisService.java
    Optional<Crianca> adicionarCrianca(UUID responsavelId, Crianca novaCrianca);

    // Em ResponsaveisService.java
    Optional<Responsaveis> update(UUID id, Responsaveis responsavelDetails);

    List<CriancaDTO> findDependentesByAuthUser(User user);
}