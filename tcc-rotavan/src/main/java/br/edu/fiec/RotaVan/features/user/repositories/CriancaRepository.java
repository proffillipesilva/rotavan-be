package br.edu.fiec.RotaVan.features.user.repositories;

import br.edu.fiec.RotaVan.features.user.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface CriancaRepository extends JpaRepository<Crianca, UUID> {
    @Query("SELECT c FROM Crianca c " +
            "JOIN Solicitacao s ON c = s.crianca " +
            "WHERE s.motorista = :motorista " +
            "AND s.status = 'ACEITO' " +
            "AND c.escola = :escola " +
            "AND c.tipoServico IN :tiposServico")
    List<Crianca> findAlunosAceitosParaRota(
            @Param("motorista") Motoristas motorista,
            @Param("escola") Escolas escola,
            @Param("tiposServico") List<TipoServico> tiposServico
    );
}