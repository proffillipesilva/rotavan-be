package br.edu.fiec.RotaVan.features.rotas.repositories; // Ajuste o pacote se necessário

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PontoRepository extends JpaRepository<Ponto, UUID> {
    // Você pode adicionar métodos de consulta personalizados aqui se precisar
    // Ex: List<Ponto> findByRotaOrderByOrdemAsc(Rota rota);
}