package br.edu.fiec.RotaVan.features.contratos.services.impl;

import br.edu.fiec.RotaVan.features.contratos.models.Contrato;
import br.edu.fiec.RotaVan.features.contratos.repositories.ContratoRepository;
import br.edu.fiec.RotaVan.features.contratos.services.ContratoService;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final ResponsaveisRepository responsaveisRepository;
    private final MotoristasRepository motoristasRepository;

    @Override
    @Transactional
    public Contrato save(Contrato contrato) {
        // Validações
        if (contrato.getDataInicio() != null && contrato.getDataFim() != null &&
                contrato.getDataFim().isBefore(contrato.getDataInicio())) {
            throw new IllegalArgumentException("Data final não pode ser anterior à data inicial.");
        }
        return contratoRepository.save(contrato);
    }

    @Override
    public List<Contrato> findAll() {
        return contratoRepository.findAll();
    }

    @Override
    public Optional<Contrato> findById(UUID id) {
        return contratoRepository.findById(id);
    }

    @Override
    public List<Contrato> findByResponsavelId(UUID responsavelId) {
        return responsaveisRepository.findById(responsavelId)
                .map(contratoRepository::findByResponsavel)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<Contrato> findByMotoristaId(UUID motoristaId) {
        return motoristasRepository.findById(motoristaId)
                .map(contratoRepository::findByMotorista)
                .orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public Optional<Contrato> update(UUID id, Contrato contratoDetails) {
        return contratoRepository.findById(id)
                .map(existingContrato -> {
                    if (contratoDetails.getDataInicio() != null) existingContrato.setDataInicio(contratoDetails.getDataInicio());
                    if (contratoDetails.getDataFim() != null) existingContrato.setDataFim(contratoDetails.getDataFim());
                    if (contratoDetails.getValorMensal() != null) existingContrato.setValorMensal(contratoDetails.getValorMensal());
                    if (contratoDetails.getStatus() != null) existingContrato.setStatus(contratoDetails.getStatus());
                    return contratoRepository.save(existingContrato);
                });
    }

    @Override
    @Transactional
    public boolean deleteById(UUID id) {
        if (contratoRepository.existsById(id)) {
            contratoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Contrato criarContrato(Responsaveis responsavel, Motoristas motorista, Crianca crianca) {
        Contrato novoContrato = new Contrato();
        novoContrato.setResponsavel(responsavel);
        novoContrato.setMotorista(motorista);
        // novoContrato.setCrianca(crianca); // Descomente se tiver adicionado o campo na entidade
        novoContrato.setDataInicio(LocalDate.now());
        novoContrato.setDataFim(LocalDate.now().plusMonths(12));
        novoContrato.setStatus("ATIVO");
        novoContrato.setValorMensal(new BigDecimal("500.00"));
        return contratoRepository.save(novoContrato);
    }
}