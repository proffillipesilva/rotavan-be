package br.edu.fiec.RotaVan.features.rotas.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.rotas.models.Viagem;
import br.edu.fiec.RotaVan.features.rotas.services.ViagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/viagens") // Caminho base
@AllArgsConstructor
@Tag(name = "Viagens", description = "API para gerenciamento e acompanhamento de Viagens (corridas)")
@SecurityRequirement(name = "bearerAuth")
public class ViagemController {

    private final ViagemService viagemService;

    @PostMapping
    @Operation(summary = "Cria (agenda) uma nova viagem",
            description = "Registra uma nova viagem no sistema, associando uma rota, motorista e data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Viagem criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para a viagem (ex: rota ou data faltando)."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Viagem> createViagem(@Valid @RequestBody Viagem viagem) {
        // Se o service lançar IllegalArgumentException ou RuntimeException,
        // o GlobalExceptionHandler vai capturar e formatar a resposta.
        Viagem savedViagem = viagemService.save(viagem);
        return new ResponseEntity<>(savedViagem, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todas as viagens",
            description = "Retorna uma lista de todas as viagens agendadas no sistema (Acesso Admin).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de viagens retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou não autorizado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Viagem>> getAllViagens() {
        return ResponseEntity.ok(viagemService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma viagem pelo ID (UUID)",
            description = "Retorna os dados de uma viagem específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Viagem encontrada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Viagem não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Viagem> getViagemById(
            @Parameter(description = "ID (UUID) da viagem a ser buscada.", required = true)
            @PathVariable UUID id) {

        return viagemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Exemplo: Buscar viagens de um motorista em uma data específica
    // GET /v1/api/viagens/motorista/{motoristaId}?data=YYYY-MM-DD
    @GetMapping("/motorista/{motoristaId}")
    @Operation(summary = "Busca viagens de um motorista por data",
            description = "Retorna uma lista de viagens de um motorista específico em uma data (YYYY-MM-DD).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de viagens retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Viagem>> getViagensByMotoristaAndData(
            @Parameter(description = "ID (UUID) do motorista.", required = true)
            @PathVariable UUID motoristaId,

            @Parameter(description = "Data da busca no formato YYYY-MM-DD.", required = true, example = "2025-11-07")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<Viagem> viagens = viagemService.findByMotoristaIdAndData(motoristaId, data);
        return ResponseEntity.ok(viagens);
    }

    // Exemplo: Buscar viagens de uma criança em um período
    // GET /v1/api/viagens/crianca/{criancaId}?inicio=YYYY-MM-DD&fim=YYYY-MM-DD
    @GetMapping("/crianca/{criancaId}")
    @Operation(summary = "Busca histórico de viagens de um dependente por período",
            description = "Retorna o histórico de viagens de um dependente (criança) entre duas datas (YYYY-MM-DD).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de viagens retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Dependente não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Viagem>> getViagensByCriancaAndPeriodo(
            @Parameter(description = "ID (UUID) do dependente (criança).", required = true)
            @PathVariable UUID criancaId,

            @Parameter(description = "Data inicial do período (YYYY-MM-DD).", required = true, example = "2025-11-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,

            @Parameter(description = "Data final do período (YYYY-MM-DD).", required = true, example = "2025-11-30")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<Viagem> viagens = viagemService.findByCriancaIdAndPeriodo(criancaId, inicio, fim);
        return ResponseEntity.ok(viagens);
    }


    // Exemplo: Endpoint específico para atualizar apenas o status
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza o status de uma viagem (ex: Iniciar, Finalizar)",
            description = "Faz uma atualização parcial na viagem, modificando apenas seu status. Enviar o status como um texto simples no corpo (ex: \"EM_ANDAMENTO\").")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da viagem atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Status inválido ou não fornecido."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Viagem não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Viagem> updateViagemStatus(
            @Parameter(description = "ID (UUID) da viagem a ser atualizada.", required = true)
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "O novo status da viagem. (Ex: 'AGENDADA', 'EM_ANDAMENTO', 'FINALIZADA', 'CANCELADA')",
                    required = true,
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string", example = "EM_ANDAMENTO")))
            @RequestBody String status) {

        // Remover aspas extras se o status vier como "Agendada" em vez de Agendada
        String cleanStatus = status.replace("\"", "");
        return viagemService.updateStatus(id, cleanStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta (cancela) uma viagem pelo ID (UUID)",
            description = "Remove o registro de uma viagem do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Viagem deletada com sucesso. (Sem conteúdo no retorno)"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Viagem não encontrada para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Void> deleteViagem(
            @Parameter(description = "ID (UUID) da viagem a ser deletada.", required = true)
            @PathVariable UUID id) {

        if (viagemService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}