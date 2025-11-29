package br.edu.fiec.RotaVan.features.user.services.impl;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.repositories.EscolasRepository;
import br.edu.fiec.RotaVan.features.user.services.EscolasService;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService; // IMPORTANTE
import com.google.maps.model.LatLng; // IMPORTANTE
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal; // IMPORTANTE
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EscolasServiceImpl implements EscolasService {

    private final EscolasRepository escolasRepository;
    private final GoogleMapsService googleMapsService; // 1. Serviço Injetado

    // 2. Construtor atualizado
    public EscolasServiceImpl(EscolasRepository escolasRepository, GoogleMapsService googleMapsService) {
        this.escolasRepository = escolasRepository;
        this.googleMapsService = googleMapsService;
    }

    @Override
    public Escolas save(Escolas escola) {
        // Opcional: Tentar geocodificar antes de salvar um cadastro manual
        if (escola.getId() == null) {
            atualizarLatLong(escola);
        }
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

                    // Se mudou o endereço, reseta as coordenadas para buscar de novo
                    if (!existingEscola.getEndereco().equalsIgnoreCase(escolaDetails.getEndereco())) {
                        existingEscola.setEndereco(escolaDetails.getEndereco());
                        atualizarLatLong(existingEscola); // Busca nova lat/long
                    } else {
                        existingEscola.setTelefone(escolaDetails.getTelefone());
                    }

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

    // --- Importação CSV com Geocoding ---
    @Override
    public void importarEscolasViaCsv(MultipartFile arquivo) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(arquivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    if (linha.toLowerCase().contains("nome") || linha.toLowerCase().contains("cnpj")) {
                        continue;
                    }
                }

                String[] dados = linha.split(",");

                if (dados.length >= 3) {
                    Escolas escola = new Escolas();
                    escola.setNome(dados[0].trim());
                    escola.setCnpj(dados[1].trim());
                    escola.setEndereco(dados[2].trim());

                    if (dados.length > 3) {
                        escola.setTelefone(dados[3].trim());
                    }

                    // 3. Busca Lat/Long antes de salvar
                    atualizarLatLong(escola);

                    try {
                        escolasRepository.save(escola);
                    } catch (Exception e) {
                        System.err.println("Erro ao importar escola (duplicata?): " + dados[0]);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Falha ao processar arquivo CSV: " + e.getMessage());
        }
    }

    // --- NOVO MÉTODO: Sincronização (para corrigir o banco atual) ---
    // Adicione "void sincronizarCoordenadas();" na interface EscolasService
    public void sincronizarCoordenadas() {
        List<Escolas> todas = escolasRepository.findAll();
        int atualizadas = 0;

        for (Escolas escola : todas) {
            // Só atualiza se estiver faltando coordenada
            if (escola.getLatitude() == null || escola.getLongitude() == null) {
                atualizarLatLong(escola);

                // Se conseguiu obter (não é mais null), salva
                if (escola.getLatitude() != null) {
                    escolasRepository.save(escola);
                    atualizadas++;
                }
            }
        }
        System.out.println("Sincronização concluída. Escolas atualizadas: " + atualizadas);
    }

    // --- Método Auxiliar Privado ---
    private void atualizarLatLong(Escolas escola) {
        if (escola.getEndereco() == null || escola.getEndereco().isEmpty()) return;

        try {
            // Dica: Concatene a cidade se o CSV não tiver, para evitar ambiguidade
            String enderecoBusca = escola.getEndereco();
            if (!enderecoBusca.toLowerCase().contains("indaiatuba")) {
                enderecoBusca += ", Indaiatuba - SP";
            }

            LatLng coords = googleMapsService.buscarCoordenadas(enderecoBusca);

            if (coords != null) {
                escola.setLatitude(BigDecimal.valueOf(coords.lat));
                escola.setLongitude(BigDecimal.valueOf(coords.lng));
            }
        } catch (Exception e) {
            // Logar erro mas não parar o processo
            System.err.println("Erro ao buscar coordenadas para: " + escola.getNome() + " - " + e.getMessage());
        }
    }
}