package br.edu.fiec.RotaVan.features.user.services;

import br.edu.fiec.RotaVan.features.user.models.Responsaveis;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface (contrato) para o serviço de Responsaveis.
 * Define todas as operações de negócio que podem ser realizadas com os responsáveis.
 * O Controller irá interagir com esta interface.
 */
public interface ResponsaveisService {

    /**
     * Salva um novo responsável na base de dados.
     * @param responsavel O objeto a ser salvo.
     * @return O objeto salvo com o seu ID preenchido.
     */
    Responsaveis criaResponsavel(Responsaveis responsavel);

    /**
     * Retorna uma lista com todos os responsáveis.
     * @return Lista de todos os Responsaveis.
     */
    List<Responsaveis> findAll();

    /**
     * Busca um responsável específico pelo seu ID.
     * @param id O ID a ser procurado.
     * @return Um Optional contendo o responsável se encontrado, ou vazio se não.
     */
    Optional<Responsaveis> findById(UUID id);
}