package br.edu.fiec.RotaVan.features.user.controllers;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.services.EscolasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Operation(summary = "Lista todas as escolas", description = "Retorna uma lista de todas as escolas cadastradas. Público.")
    @GetMapping
    public ResponseEntity<List<Escolas>> getAllEscolas() {
        return ResponseEntity.ok(escolasService.findAll());
    }

    @Operation(summary = "Busca uma escola pelo ID", description = "Retorna os dados de uma escola específica.")
    @GetMapping("/{id}")
    public ResponseEntity<Escolas> getEscolaById(@PathVariable UUID id) {
        return escolasService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualiza uma escola", description = "Atualiza os dados de uma escola existente. Requer Admin.")
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<Escolas> updateEscola(@PathVariable UUID id, @Valid @RequestBody Escolas escolaDetails) {
        return escolasService.update(id, escolaDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta uma escola", description = "Remove uma escola. Requer Admin.")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEscola(@PathVariable UUID id) {
        if (escolasService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- NOVO ENDPOINT PARA IMPORTAÇÃO ---
    @Operation(summary = "Importar Escolas via CSV",
            description = "Faz o upload de um arquivo .csv. Ordem das colunas: Nome, CNPJ, Endereço, Telefone(opcional).")
    @SecurityRequirement(name = "bearerAuth") // Protegido com cadeado
    @PostMapping(value = "/upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("O arquivo está vazio.");
        }

        try {
            escolasService.importarEscolasViaCsv(file);
            return ResponseEntity.ok("Processo de importação concluído!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao importar: " + e.getMessage());
        }
    }
}