package br.edu.fiec.RotaVan.features.solicitacao.repositories;

import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Não se esqueça deste import!
import java.util.UUID;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, UUID> {

    // O Spring cria o SQL automaticamente baseado no nome destes métodos:

    // 1. Busca pelo campo 'status' (String)
    List<Solicitacao> findByStatus(String status);

    // 2. Busca pelo ID do objeto 'motorista' (Relacionamento)
    // O Spring entende que deve ir em Solicitacao -> getMotorista() -> getId()
    List<Solicitacao> findByMotoristaId(UUID motoristaId);

    // 3. Busca pelo ID do objeto 'responsavel' (Relacionamento)
    List<Solicitacao> findByResponsavelId(UUID responsavelId);
}