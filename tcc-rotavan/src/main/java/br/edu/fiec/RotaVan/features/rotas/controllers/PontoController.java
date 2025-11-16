package br.edu.fiec.RotaVan.features.rotas.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Ponto;
import br.edu.fiec.RotaVan.features.rotas.services.PontoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/pontos") // Caminho base para pontos individuais
@AllArgsConstructor
@Tag(name = "Rotas - Pontos", description = "API para gerenciamento individual de Pontos de uma Rota")
@SecurityRequirement(name = "bearerAuth") // Protege todos os endpoints deste controller
public class PontoController {

    private final PontoService pontoService;

    // Não faz sentido criar um ponto sem rota, então não teremos POST aqui.
    // A criação é feita via POST /v1/api/rotas/{rotaId}/pontos

    @GetMapping("/{id}")
    @Operation(summary = "Busca um ponto específico pelo ID (UUID)",
            description = "Retorna os dados de um ponto de parada específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ponto encontrado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Ponto não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Ponto> getPontoById(
            @Parameter(description = "ID (UUID) do ponto a ser buscado.", required = true)
            @PathVariable UUID id) {

        return pontoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um ponto específico pelo ID (UUID)",
            description = "Atualiza os dados de um ponto de parada (ex: endereço, ordem).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ponto atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Ponto não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Ponto> updatePonto(
            @Parameter(description = "ID (UUID) do ponto a ser atualizado.", required = true)
            @PathVariable UUID id,

            @Valid @RequestBody Ponto pontoDetails) {

        // Nota: Este update não altera a rota do ponto.
        return pontoService.update(id, pontoDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um ponto específico pelo ID (UUID)",
            description = "Remove um ponto de parada do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ponto deletado com sucesso. (Sem conteúdo no retorno)"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Ponto não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Void> deletePonto(
            @Parameter(description = "ID (UUID) do ponto a ser deletado.", required = true)
            @PathVariable UUID id) {

        if (pontoService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}