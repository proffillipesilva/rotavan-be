package br.edu.fiec.RotaVan.features.rotas.services; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Viagem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ViagemService {
    Viagem save(Viagem viagem);
    List<Viagem> findAll();
    Optional<Viagem> findById(UUID id);
    List<Viagem> findByMotoristaIdAndData(UUID motoristaId, LocalDate data);
    List<Viagem> findByCriancaIdAndPeriodo(UUID criancaId, LocalDate dataInicio, LocalDate dataFim);
    Optional<Viagem> updateStatus(UUID id, String status); // Exemplo de método específico
    boolean deleteById(UUID id);
}