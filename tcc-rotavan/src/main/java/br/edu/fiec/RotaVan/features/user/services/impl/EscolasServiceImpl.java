package br.edu.fiec.RotaVan.features.user.services.impl;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.repositories.EscolasRepository;
import br.edu.fiec.RotaVan.features.user.services.EscolasService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EscolasServiceImpl implements EscolasService {

    private final EscolasRepository escolasRepository;

    public EscolasServiceImpl(EscolasRepository escolasRepository) {
        this.escolasRepository = escolasRepository;
    }

    @Override
    public Escolas save(Escolas escola) {
        return escolasRepository.save(escola);
    }

    @Override
    public List<Escolas> findAll() {
        return escolasRepository.findAll();
    }

    @Override
    public Optional<Escolas> findById(UUID id) {
        return escolasRepository.findById(id);
    }

    @Override
    public Optional<Escolas> update(UUID id, Escolas escolaDetails) {
        return escolasRepository.findById(id)
                .map(existingEscola -> {
                    existingEscola.setNome(escolaDetails.getNome());
                    existingEscola.setCnpj(escolaDetails.getCnpj());
                    existingEscola.setEndereco(escolaDetails.getEndereco());
                    existingEscola.setTelefone(escolaDetails.getTelefone());
                    return escolasRepository.save(existingEscola);
                });
    }

    @Override
    public boolean deleteById(UUID id) {
        if (escolasRepository.existsById(id)) {
            escolasRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Implementação da Importação CSV ---
    @Override
    public void importarEscolasViaCsv(MultipartFile arquivo) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(arquivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {
                // Ignora cabeçalho se houver
                if (primeiraLinha) {
                    primeiraLinha = false;
                    // Verifica se a primeira linha parece um cabeçalho (contém "Nome" ou "CNPJ")
                    if (linha.toLowerCase().contains("nome") || linha.toLowerCase().contains("cnpj")) {
                        continue;
                    }
                }

                // Divide por vírgula
                String[] dados = linha.split(",");

                // Verifica se tem as 3 colunas obrigatórias: Nome, CNPJ, Endereço
                if (dados.length >= 3) {
                    Escolas escola = new Escolas();
                    escola.setNome(dados[0].trim());
                    escola.setCnpj(dados[1].trim());
                    escola.setEndereco(dados[2].trim());

                    // A 4ª coluna (Telefone) é opcional
                    if (dados.length > 3) {
                        escola.setTelefone(dados[3].trim());
                    }

                    // Tenta salvar. Se o CNPJ já existir, o banco dá erro, então usamos try-catch
                    try {
                        escolasRepository.save(escola);
                    } catch (Exception e) {
                        System.err.println("Erro ao importar escola (possível duplicata): " + dados[0] + " - " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao processar arquivo CSV: " + e.getMessage());
        }
    }
}