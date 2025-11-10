package br.edu.fiec.RotaVan.features.user.controllers;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.services.EscolasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Escola criada com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos. Verifique os campos obrigatórios."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou não autorizado."),
            @ApiResponse(responseCode = "409", description = "Conflito. Uma escola com este nome ou CNPJ já existe."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<Escolas> createEscola(@RequestBody Escolas escola) {
        Escolas savedEscola = escolasService.save(escola);
        return new ResponseEntity<>(savedEscola, HttpStatus.CREATED);
    }

    @Operation(summary = "Lista todas as escolas",
            description = "Retorna uma lista de todas as escolas cadastradas. Este endpoint é público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escolas retornada com sucesso (pode ser uma lista vazia)."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    public ResponseEntity<List<Escolas>> getAllEscolas() {
        return ResponseEntity.ok(escolasService.findAll());
    }

    @Operation(summary = "Busca uma escola pelo ID (UUID)",
            description = "Retorna os dados de uma escola específica. Este endpoint é público.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escola encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escola atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou não autorizado."),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "409", description = "Conflito (ex: nome/CNPJ já pertence a outra escola)."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<Escolas> updateEscola(
            @Parameter(description = "ID (UUID) da escola a ser atualizada.", required = true)
            @PathVariable UUID id, @RequestBody Escolas escolaDetails) {
        return escolasService.update(id, escolaDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta uma escola pelo ID (UUID)",
            description = "Remove o registro de uma escola do sistema. Requer autenticação de administrador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Escola deletada com sucesso. (Sem conteúdo no retorno)"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou não autorizado."),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
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