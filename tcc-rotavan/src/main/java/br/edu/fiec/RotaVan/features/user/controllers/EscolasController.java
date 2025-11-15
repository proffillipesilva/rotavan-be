package br.edu.fiec.RotaVan.features.user.controllers;

import br.edu.fiec.RotaVan.features.user.dto.EscolaRegisterRequest; // <-- 1. IMPORTAR O DTO
import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.services.EscolasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // <-- 2. IMPORTAR VALID
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/escolas")
@Tag(name = "Escolas", description = "API para gerenciamento de Escolas")
public class EscolasController {

    private final EscolasService escolasService;

    public EscolasController(EscolasService escolasService) {
        this.escolasService = escolasService;
    }

    @Operation(summary = "Cria uma nova escola",
            description = "Cadastra uma nova escola no sistema. Requer autenticação de administrador.")
    // ... (ApiResponses) ...
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<Escolas> createEscola(@Valid @RequestBody EscolaRegisterRequest escolaRequest) { // <-- 3. MUDAR AQUI

        // 4. Mapear do DTO para a Entidade
        Escolas novaEscola = new Escolas();
        novaEscola.setNome(escolaRequest.getNome());
        novaEscola.setEmail(escolaRequest.getEmail()); // <-- Agora inclui o email
        novaEscola.setCnpj(escolaRequest.getCnpj());
        novaEscola.setEndereco(escolaRequest.getEndereco());
        novaEscola.setTelefone(escolaRequest.getTelefone());

        // 5. Salvar a nova entidade
        Escolas savedEscola = escolasService.save(novaEscola);
        return new ResponseEntity<>(savedEscola, HttpStatus.CREATED);
    }

    @Operation(summary = "Lista todas as escolas",
            description = "Retorna uma lista de todas as escolas cadastradas. Este endpoint é público.")
    // ... (ApiResponses) ...
    @GetMapping
    public ResponseEntity<List<Escolas>> getAllEscolas() {
        return ResponseEntity.ok(escolasService.findAll());
    }

    @Operation(summary = "Busca uma escola pelo ID (UUID)",
            description = "Retorna os dados de uma escola específica. Este endpoint é público.")
    // ... (ApiResponses) ...
    @GetMapping("/{id}")
    public ResponseEntity<Escolas> getEscolaById(
            @Parameter(description = "ID (UUID) da escola a ser buscada.", required = true)
            @PathVariable UUID id) {
        return escolasService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualiza uma escola pelo ID (UUID)",
            description = "Atualiza os dados de uma escola existente. Requer autenticação de administrador.")
    // ... (ApiResponses) ...
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<Escolas> updateEscola(
            @Parameter(description = "ID (UUID) da escola a ser atualizada.", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody Escolas escolaDetails) { // <-- 6. Manter @Valid e Escolas aqui

        // O método update já foi corrigido no ServiceImpl para salvar o email
        return escolasService.update(id, escolaDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta uma escola pelo ID (UUID)",
            description = "Remove o registro de uma escola do sistema. Requer autenticação de administrador.")
    // ... (ApiResponses) ...
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEscola(
            @Parameter(description = "ID (UUID) da escola a ser deletada.", required = true)
            @PathVariable UUID id) {
        if (escolasService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}