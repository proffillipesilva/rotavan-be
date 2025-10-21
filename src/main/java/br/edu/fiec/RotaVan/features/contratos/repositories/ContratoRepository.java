package br.edu.fiec.RotaVan.features.contratos.repositories; // Ajuste o pacote se necessário

import br.edu.fiec.RotaVan.features.contratos.models.Contrato;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, UUID> {
    // Exemplos de métodos personalizados:
    List<Contrato> findByResponsavel(Responsaveis responsavel);
    List<Contrato> findByMotorista(Motoristas motorista);
}