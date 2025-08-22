package br.edu.fiec.RotaVan.services.impl;

import br.edu.fiec.RotaVan.models.Responsaveis;
import br.edu.fiec.RotaVan.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.services.ResponsaveisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A anotação @Service marca esta classe como um componente de serviço do Spring.
 * É aqui que a lógica de negócio é implementada.
 * Esta classe implementa a interface ResponsaveisService.
 */
@Service
public class ResponsaveisServiceImpl implements ResponsaveisService {

    // Injeção de dependência do repositório
    private final ResponsaveisRepository responsaveisRepository;

    @Autowired // Esta anotação é opcional em construtores a partir de versões mais recentes do Spring
    public ResponsaveisServiceImpl(ResponsaveisRepository responsaveisRepository) {
        this.responsaveisRepository = responsaveisRepository;
    }

    @Override
    public Responsaveis criaResponsavel(Responsaveis responsavel) {
        // Futuramente, aqui podem ser adicionadas regras de negócio.
        // Ex: verificar se o email já existe antes de salvar.
        return responsaveisRepository.save(responsavel);
    }

    @Override
    public List<Responsaveis> findAll() {
        return responsaveisRepository.findAll();
    }

    @Override
    public Optional<Responsaveis> findById(UUID id) {
        return responsaveisRepository.findById(id);
    }
}