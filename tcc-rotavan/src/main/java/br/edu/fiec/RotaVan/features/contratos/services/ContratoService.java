package br.edu.fiec.RotaVan.features.contratos.services; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.contratos.models.Contrato;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContratoService {
    Contrato save(Contrato contrato);
    List<Contrato> findAll();
    Optional<Contrato> findById(UUID id);
    List<Contrato> findByResponsavelId(UUID responsavelId);
    List<Contrato> findByMotoristaId(UUID motoristaId);
    Optional<Contrato> update(UUID id, Contrato contratoDetails);
    boolean deleteById(UUID id);
}