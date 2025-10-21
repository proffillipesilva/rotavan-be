package br.edu.fiec.RotaVan.features.rotas.repositories; // Ajuste o pacote se necessário

import br.edu.fiec.RotaVan.features.rotas.models.Viagem;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, UUID> {
    // Exemplos de métodos personalizados:
    List<Viagem> findByData(LocalDate data);
    List<Viagem> findByMotoristaAndData(Motoristas motorista, LocalDate data);
    List<Viagem> findByCriancaAndDataBetween(Crianca crianca, LocalDate dataInicio, LocalDate dataFim);
}