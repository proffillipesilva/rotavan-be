package br.edu.fiec.RotaVan.features.user.repositories;

import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CriancaRepository extends JpaRepository<Crianca, UUID> {

    // --- INÍCIO DO CÓDIGO ADICIONADO ---

    /**
     * Esta é a "Query Principal (Alunos Antigos)" descrita no Guia (Passo 2A e 2B).
     * [cite: 26, 36]
     *
     * Ela busca todas as crianças (Dependentes) que:
     * 1. Pertencem a um motorista específico.
     * 2. Já tiveram a solicitação ACEITA.
     * 3. Vão para a mesma escola.
     * 4. Usam os tipos de serviço relevantes (ex: IDA ou IDA_E_VOLTA).
     */
    @Query("SELECT c FROM Crianca c " +
            "JOIN Solicitacao s ON c = s.crianca " + // Faz o JOIN com Solicitacao
            "WHERE s.motorista = :motorista " +
            "AND s.status = 'ACEITO' " + // Filtra apenas alunos já aceites
            "AND c.escola = :escola " +
            "AND c.tipoServico IN :tiposServico") // Filtra por 'IDA' ou 'VOLTA'
    List<Crianca> findAlunosAceitosParaRota(
            @Param("motorista") Motoristas motorista,
            @Param("escola") Escolas escola,
            @Param("tiposServico") List<TipoServico> tiposServico
    );

    // --- FIM DO CÓDIGO ADICIONADO ---
}