package br.edu.fiec.RotaVan.features.rotas.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.rotas.services.RotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // Para validar o corpo da requisição
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/rotas") // Define o caminho base
@AllArgsConstructor // Injeta o RotaService via construtor
@Tag(name = "Rotas", description = "API para gerenciamento de Rotas e seus Pontos")
@SecurityRequirement(name = "bearerAuth") // Protege todos os endpoints
public class RotaController {

    private final RotaService rotaService;

    @PostMapping
    @Operation(summary = "Cria uma nova rota",
            description = "Cria uma nova rota e, opcionalmente, seus pontos de parada associados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rota criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para a rota."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Rota> createRota(@Valid @RequestBody Rota rota) {
        // A lógica de salvar os pontos associados já está no RotaServiceImpl
        Rota savedRota = rotaService.save(rota);
        return new ResponseEntity<>(savedRota, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todas as rotas",
            description = "Retorna uma lista de todas as rotas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de rotas retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Rota>> getAllRotas() {
        return ResponseEntity.ok(rotaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma rota pelo ID (UUID)",
            description = "Retorna os dados de uma rota específica, incluindo seus pontos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rota encontrada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Rota não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Rota> getRotaById(
            @Parameter(description = "ID (UUID) da rota a ser buscada.", required = true)
            @PathVariable UUID id) {

        return rotaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma rota pelo ID (UUID)",
            description = "Atualiza os dados de uma rota (ex: nome, turno).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rota atualizada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Rota não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Rota> updateRota(
            @Parameter(description = "ID (UUID) da rota a ser atualizada.", required = true)
            @PathVariable UUID id,

            @Valid @RequestBody Rota rotaDetails) {

        return rotaService.update(id, rotaDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma rota pelo ID (UUID)",
            description = "Remove uma rota e todos os seus pontos associados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rota deletada com sucesso. (Sem conteúdo no retorno)"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Rota não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Void> deleteRota(
            @Parameter(description = "ID (UUID) da rota a ser deletada.", required = true)
            @PathVariable UUID id) {

        if (rotaService.deleteById(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }

    // --- Endpoints específicos para Pontos dentro de uma Rota ---

    @PostMapping("/{rotaId}/pontos")
    @Operation(summary = "Adiciona um novo ponto a uma rota",
            description = "Cria um novo ponto de parada e o associa a uma rota existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ponto criado e associado à rota com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para o ponto."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Rota não encontrada com o ID (rotaId) fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Ponto> addPontoToRota(
            @Parameter(description = "ID (UUID) da rota que receberá o ponto.", required = true)
            @PathVariable UUID rotaId,

            @Valid @RequestBody Ponto ponto) {

        return rotaService.adicionarPontoNaRota(rotaId, ponto)
                .map(savedPonto -> new ResponseEntity<>(savedPonto, HttpStatus.CREATED))
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 se a rota não existir
    }

    @GetMapping("/{rotaId}/pontos")
    @Operation(summary = "Lista todos os pontos de uma rota",
            description = "Retorna uma lista de todos os pontos de parada associados a uma rota específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pontos retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Rota não encontrada com o ID (rotaId) fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Ponto>> getPontosByRotaId(
            @Parameter(description = "ID (UUID) da rota para buscar os pontos.", required = true)
            @PathVariable UUID rotaId) {

        // Verifica se a rota existe antes de buscar os pontos (opcional, mas bom)
        if (rotaService.findById(rotaId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Ponto> pontos = rotaService.findPontosByRotaId(rotaId); // Usa o método do service
        return ResponseEntity.ok(pontos);
    }

    // Você precisaria adicionar métodos no RotaService e PontoService/Repository
    // para atualizar ou deletar um ponto específico de uma rota, se necessário.
    // Ex: PUT /v1/api/rotas/{rotaId}/pontos/{pontoId}
    // Ex: DELETE /v1/api/rotas/{rotaId}/pontos/{pontoId}
}