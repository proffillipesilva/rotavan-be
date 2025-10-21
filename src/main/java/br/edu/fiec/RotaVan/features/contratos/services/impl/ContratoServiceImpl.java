package br.edu.fiec.RotaVan.features.contratos.services.impl; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.contratos.models.Contrato;
import br.edu.fiec.RotaVan.features.contratos.repositories.ContratoRepository;
import br.edu.fiec.RotaVan.features.contratos.services.ContratoService;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository; // Necessário para buscar por ID
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository; // Necessário para buscar por ID
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    // Injetar repositórios necessários para buscar Responsavel/Motorista por ID
    private final ResponsaveisRepository responsaveisRepository;
    private final MotoristasRepository motoristasRepository;


    public ContratoServiceImpl(ContratoRepository contratoRepository,
                               ResponsaveisRepository responsaveisRepository,
                               MotoristasRepository motoristasRepository) {
        this.contratoRepository = contratoRepository;
        this.responsaveisRepository = responsaveisRepository;
        this.motoristasRepository = motoristasRepository;
    }

    @Override
    @Transactional
    public Contrato save(Contrato contrato) {
        // Validações: Ex: Data fim não pode ser antes da data início
        if (contrato.getDataFim().isBefore(contrato.getDataInicio())) {
            throw new IllegalArgumentException("Data final não pode ser anterior à data inicial.");
        }
        // Verificar se Responsavel e Motorista existem antes de salvar
        responsaveisRepository.findById(contrato.getResponsavel().getId())
                .orElseThrow(() -> new RuntimeException("Responsável não encontrado."));
        motoristasRepository.findById(contrato.getMotorista().getId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado."));

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
        Optional<Responsaveis> responsavelOpt = responsaveisRepository.findById(responsavelId);
        // O método findByResponsavel pode ser gerado no ContratoRepository
        return responsavelOpt.map(contratoRepository::findByResponsavel).orElse(Collections.emptyList());
    }

    @Override
    public List<Contrato> findByMotoristaId(UUID motoristaId) {
        Optional<Motoristas> motoristaOpt = motoristasRepository.findById(motoristaId);
        // O método findByMotorista pode ser gerado no ContratoRepository
        return motoristaOpt.map(contratoRepository::findByMotorista).orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public Optional<Contrato> update(UUID id, Contrato contratoDetails) {
        return contratoRepository.findById(id)
                .map(existingContrato -> {
                    if (contratoDetails.getDataFim().isBefore(contratoDetails.getDataInicio())) {
                        throw new IllegalArgumentException("Data final não pode ser anterior à data inicial.");
                    }
                    existingContrato.setDataInicio(contratoDetails.getDataInicio());
                    existingContrato.setDataFim(contratoDetails.getDataFim());
                    existingContrato.setValorMensal(contratoDetails.getValorMensal());
                    // Geralmente não se altera responsável ou motorista de um contrato existente,
                    // mas se precisar, adicione aqui (com validação de existência).
                    // existingContrato.setResponsavel(contratoDetails.getResponsavel());
                    // existingContrato.setMotorista(contratoDetails.getMotorista());
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
}