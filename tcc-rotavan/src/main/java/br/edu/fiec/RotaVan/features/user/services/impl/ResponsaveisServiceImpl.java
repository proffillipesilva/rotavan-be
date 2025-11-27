package br.edu.fiec.RotaVan.features.user.services.impl;

import br.edu.fiec.RotaVan.features.user.dto.CriancaDTO;
import br.edu.fiec.RotaVan.features.user.dto.CriancaRequestDTO;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.repositories.CriancaRepository;
import br.edu.fiec.RotaVan.features.user.repositories.EscolasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.features.user.services.ResponsaveisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResponsaveisServiceImpl implements ResponsaveisService {

    private final ResponsaveisRepository responsaveisRepository;
    private final CriancaRepository criancaRepository;
    private final EscolasRepository escolasRepository;

    @Override
    @Transactional
    public Responsaveis criaResponsavel(Responsaveis responsavel) {
        if (responsavel.getCriancas() != null) {
            for (Crianca crianca : responsavel.getCriancas()) {
                crianca.setResponsavel(responsavel);
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
    public Optional<Crianca> adicionarCrianca(User user, CriancaRequestDTO novaCrianca) {

        Escolas escola =  escolasRepository.findById(UUID.fromString(novaCrianca.getEscolaId())).orElseThrow();
        Crianca criancaBanco = new Crianca();
        criancaBanco.setNome(novaCrianca.getNome());
        criancaBanco.setEndereco(novaCrianca.getEndereco());
        criancaBanco.setNivelEscolar(novaCrianca.getNivelEscolar());
        criancaBanco.setDataNascimento(novaCrianca.getDataNascimento());
        criancaBanco.setEscola(escola);
        Crianca crianca = criancaRepository.save(criancaBanco);

        return responsaveisRepository.findByUser(user).map(responsavel -> {
            crianca.setResponsavel(responsavel);
            return criancaRepository.save(crianca);
        });
    }

    @Override
    @Transactional
    public Optional<Responsaveis> update(UUID id, Responsaveis responsavelDetails) {
        return responsaveisRepository.findById(id)
                .map(existingResponsavel -> {
                    existingResponsavel.setNomeResponsavel(responsavelDetails.getNomeResponsavel());
                    existingResponsavel.setCpfResponsavel(responsavelDetails.getCpfResponsavel());
                    existingResponsavel.setEnderecoCasa(responsavelDetails.getEnderecoCasa());
                    return responsaveisRepository.save(existingResponsavel);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<CriancaDTO> findDependentesByAuthUser(User user) {
        // 1. Encontra o perfil do responsável logado
        Responsaveis responsavel = responsaveisRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Perfil de responsável não encontrado para o usuário."));

        // 2. Converte a lista de Entidades (Crianca) para DTOs (CriancaDTO)
        // A lista já existe na entidade Responsaveis (List<Crianca> criancas)
        return responsavel.getCriancas().stream()
                .map(this::convertCriancaToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CriancaDTO findDependentesById(User user, UUID id) {
        Responsaveis responsavel = responsaveisRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Perfil de responsável não encontrado para o usuário."));
         Crianca crianca = criancaRepository.findById(id).orElseThrow();
         if(crianca.getResponsavel().getId().equals(responsavel.getId())){
             return convertCriancaToDTO(crianca);
         } else {
             throw new RuntimeException();
         }
    }

    /**
     * Método helper (auxiliar) privado para converter a Entidade Crianca
     * no DTO CriancaDTO, que será enviado ao frontend.
     */
    private CriancaDTO convertCriancaToDTO(Crianca crianca) {
        return CriancaDTO.builder()
                .id(crianca.getId())
                .nome(crianca.getNome())
                .escola(crianca.getEscola()) // <-- Agora passa o objeto Escola
                .dataNascimento(crianca.getDataNascimento())
                .nivelEscolar(crianca.getNivelEscolar())
                .endereco(crianca.getEndereco())
                // O 'telefone' foi removido (corretamente)
                .tipoServico(crianca.getTipoServico())
                .periodo(crianca.getPeriodo())
                .latitude(crianca.getLatitude())
                .longitude(crianca.getLongitude())
                .build();
    }

}