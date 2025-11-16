package br.edu.fiec.RotaVan.features.solicitacao.repositories;

import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, UUID> {
    // Você pode adicionar queries customizadas aqui se precisar,
    // ex: List<Solicitacao> findByMotoristaAndStatus(Motoristas motorista, String status);
}