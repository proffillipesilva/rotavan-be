package br.edu.fiec.RotaVan.features.user.services.impl;

import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.repositories.CriancaRepository; // IMPORTAR
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.features.user.services.ResponsaveisService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // IMPORTAR

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResponsaveisServiceImpl implements ResponsaveisService, UserDetailsService {

    private final ResponsaveisRepository responsaveisRepository;
    private final CriancaRepository criancaRepository; // INJETAR O REPOSITÓRIO DE CRIANÇA

    // ATUALIZAR O CONSTRUTOR
    public ResponsaveisServiceImpl(ResponsaveisRepository responsaveisRepository, CriancaRepository criancaRepository) {
        this.responsaveisRepository = responsaveisRepository;
        this.criancaRepository = criancaRepository;
    }

    @Override
    @Transactional // Garante que toda a operação (salvar pai e filhos) seja atômica
    public Responsaveis criaResponsavel(Responsaveis responsavel) {
        // MUITO IMPORTANTE: Definir a referência do pai em cada filho.
        // Isso garante que a chave estrangeira `responsavel_id` seja preenchida na tabela de crianças.
        if (responsavel.getCriancas() != null) {
            for (Crianca crianca : responsavel.getCriancas()) {
                crianca.setResponsavel(responsavel); // "Diz" para cada filho quem é o seu pai
            }
        }
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

    @Override
    @Transactional
    public Optional<Crianca> adicionarCrianca(UUID responsavelId, Crianca novaCrianca) {
        // 1. Busca o responsável pelo ID.
        return responsaveisRepository.findById(responsavelId).map(responsavel -> {
            // 2. Associa a nova criança ao responsável encontrado.
            novaCrianca.setResponsavel(responsavel);
            // 3. Salva a nova criança no seu próprio repositório.
            return criancaRepository.save(novaCrianca);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o responsável pelo email. Se não encontrar, lança uma exceção padrão do Spring Security.
        return responsaveisRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
    }

    @Override
    @Transactional
    public Optional<Responsaveis> update(UUID id, Responsaveis responsavelDetails) {
        return responsaveisRepository.findById(id)
                .map(existingResponsavel -> {
                    // Atualiza apenas os campos do responsável
                    existingResponsavel.setEmail(responsavelDetails.getEmail());
                    existingResponsavel.setNomeResponsavel(responsavelDetails.getNomeResponsavel());
                    existingResponsavel.setCpfResponsavel(responsavelDetails.getCpfResponsavel());
                    existingResponsavel.setEnderecoCasa(responsavelDetails.getEnderecoCasa());
                    // Nota: A senha e a lista de crianças não são atualizadas aqui por design.
                    return responsaveisRepository.save(existingResponsavel);
                });
    }
}