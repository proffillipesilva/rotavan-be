package br.edu.fiec.RotaVan.features.solicitacao.controllers;

import br.edu.fiec.RotaVan.features.rotas.models.Rota;
import br.edu.fiec.RotaVan.features.solicitacao.dto.DecisaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.dto.SolicitacaoRequestDTO;
import br.edu.fiec.RotaVan.features.solicitacao.models.Solicitacao;
import br.edu.fiec.RotaVan.features.solicitacao.services.SolicitacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/solicitacoes")
@AllArgsConstructor
@Tag(name = "Solicitações", description = "API para o fluxo de solicitação de vaga (Responsável -> Motorista)")
@SecurityRequirement(name = "bearerAuth")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    /**
     * PASSO 1 e 2: Responsável solicita a vaga.
     */
    @PostMapping
    @Operation(summary = "Cria uma nova solicitação de vaga",
            description = "Endpoint para o Responsável criar uma nova solicitação de vaga para um Motorista.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitação criada com sucesso, rotas sugeridas geradas."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição (ex: IDs faltando)."),
            @ApiResponse(responseCode = "401", description = "Usuário (Responsável) não autenticado."),
            @ApiResponse(responseCode = "404", description = "Motorista, Dependente ou Escola não encontrados com os IDs fornecidos."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Solicitacao> criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO request) {
        // Esta é a chamada correta para o método que implementamos
        Solicitacao solicitacao = solicitacaoService.criarSolicitacaoEGerarRotas(request);
        return new ResponseEntity<>(solicitacao, HttpStatus.CREATED);
    }

    /**
     * PASSO 3 (View do Motorista): Motorista vê as rotas sugestão da solicitação.
     */
    @GetMapping("/{id}/rotas")
    @Operation(summary = "Lista as rotas sugeridas de uma solicitação",
            description = "Endpoint para o Motorista visualizar as rotas sugeridas para uma solicitação pendente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de rotas sugeridas retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário (Motorista) não autenticado."),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada com o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Rota>> getRotasDaSolicitacao(
            @Parameter(description = "ID (UUID) da solicitação.", required = true)
            @PathVariable UUID id) {

        Solicitacao solicitacao = solicitacaoService.findById(id).orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));
        return ResponseEntity.ok(solicitacao.getRotasSugeridas());
    }

    // --- CORREÇÃO: O @PostMapping duplicado abaixo foi removido ---
    // (Ele chamava o método "criarSolicitacaoComRotas" que vamos remover)

    /**
     * PASSO 3 e 4: Motorista Aceita ou Recusa.
     */
    @PatchMapping("/{id}/decisao")
    @Operation(summary = "Aceita ou Recusa uma solicitação (Motorista)",
            description = "Endpoint para o Motorista tomar uma decisão (ACEITA ou RECUSADA) sobre uma solicitação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Decisão registrada com sucesso, status da solicitação atualizado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição (ex: status de decisão inválido)."),
            @ApiResponse(responseCode = "401", description = "Usuário (Motorista) não autenticado."),
            @ApiResponse(responseCode = "404", description = "Solicitação não encontrada com o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Solicitacao> decidirSolicitacao(
            @Parameter(description = "ID (UUID) da solicitação a ser decidida.", required = true)
            @PathVariable UUID id,

            @Valid @RequestBody DecisaoRequestDTO request) {

        Solicitacao solicitacao = solicitacaoService.decidirSolicitacao(id, request);
        return ResponseEntity.ok(solicitacao);
    }
}