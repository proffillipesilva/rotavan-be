package br.edu.fiec.RotaVan.features.rotas.services.impl; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Viagem;
import br.edu.fiec.RotaVan.features.rotas.repositories.ViagemRepository;
import br.edu.fiec.RotaVan.features.rotas.services.ViagemService;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.repositories.CriancaRepository; // Necessário
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository; // Necessário
import br.edu.fiec.RotaVan.features.rotas.repositories.RotaRepository; // Necessário
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ViagemServiceImpl implements ViagemService {

    private final ViagemRepository viagemRepository;
    private final MotoristasRepository motoristasRepository;
    private final CriancaRepository criancaRepository;
    private final RotaRepository rotaRepository; // Adicionar se precisar validar a Rota

    public ViagemServiceImpl(ViagemRepository viagemRepository,
                             MotoristasRepository motoristasRepository,
                             CriancaRepository criancaRepository,
                             RotaRepository rotaRepository) {
        this.viagemRepository = viagemRepository;
        this.motoristasRepository = motoristasRepository;
        this.criancaRepository = criancaRepository;
        this.rotaRepository = rotaRepository;
    }

    @Override
    @Transactional
    public Viagem save(Viagem viagem) {
        // Validar existência de Motorista, Crianca e Rota
        motoristasRepository.findById(viagem.getMotorista().getId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado."));
        criancaRepository.findById(viagem.getCrianca().getId())
                .orElseThrow(() -> new RuntimeException("Criança não encontrada."));
        rotaRepository.findById(viagem.getRota().getId())
                .orElseThrow(() -> new RuntimeException("Rota não encontrada."));

        // Outras validações (ex: hora de chegada não pode ser antes da saída)
        if (viagem.getHoraChegada() != null && viagem.getHoraChegada().isBefore(viagem.getHoraSaida())) {
            throw new IllegalArgumentException("Hora de chegada não pode ser anterior à hora de saída.");
        }
        return viagemRepository.save(viagem);
    }

    @Override
    public List<Viagem> findAll() {
        return viagemRepository.findAll();
    }

    @Override
    public Optional<Viagem> findById(UUID id) {
        return viagemRepository.findById(id);
    }

    @Override
    public List<Viagem> findByMotoristaIdAndData(UUID motoristaId, LocalDate data) {
        Optional<Motoristas> motoristaOpt = motoristasRepository.findById(motoristaId);
        // Criar método findByMotoristaAndData no ViagemRepository
        return motoristaOpt.map(m -> viagemRepository.findByMotoristaAndData(m, data))
                .orElse(Collections.emptyList());
    }

    @Override
    public List<Viagem> findByCriancaIdAndPeriodo(UUID criancaId, LocalDate dataInicio, LocalDate dataFim) {
        Optional<Crianca> criancaOpt = criancaRepository.findById(criancaId);
        // Criar método findByCriancaAndDataBetween no ViagemRepository
        return criancaOpt.map(c -> viagemRepository.findByCriancaAndDataBetween(c, dataInicio, dataFim))
                .orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public Optional<Viagem> updateStatus(UUID id, String status) {
        return viagemRepository.findById(id)
                .map(viagem -> {
                    viagem.setStatus(status);
                    // Poderia adicionar lógica aqui, ex: se status for "Concluída", setar horaChegada
                    return viagemRepository.save(viagem);
                });
    }

    @Override
    @Transactional
    public boolean deleteById(UUID id) {
        if (viagemRepository.existsById(id)) {
            viagemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}