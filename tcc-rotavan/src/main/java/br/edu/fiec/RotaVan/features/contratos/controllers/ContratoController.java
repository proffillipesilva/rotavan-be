package br.edu.fiec.RotaVan.features.contratos.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.contratos.models.Contrato;
import br.edu.fiec.RotaVan.features.contratos.services.ContratoService;
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
@RequestMapping("/v1/api/contratos") // Caminho base
@AllArgsConstructor
@Tag(name = "Contratos", description = "API para gerenciamento de Contratos de serviço")
@SecurityRequirement(name = "bearerAuth") // Aplica segurança a todos os endpoints do controller
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping
    @Operation(summary = "Cria um novo contrato",
            description = "Registra um novo contrato entre um responsável e um motorista para um dependente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contrato criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos para o contrato."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "409", description = "Conflito (ex: contrato já existe para este dependente/motorista)."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Contrato> createContrato(@Valid @RequestBody Contrato contrato) {
        // O GlobalExceptionHandler vai capturar os erros
        Contrato savedContrato = contratoService.save(contrato);
        return new ResponseEntity<>(savedContrato, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todos os contratos (Acesso Admin)",
            description = "Retorna uma lista de todos os contratos no sistema. (Requer permissão de Admin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou não autorizado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Contrato>> getAllContratos() {
        return ResponseEntity.ok(contratoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um contrato pelo ID (UUID)",
            description = "Retorna os dados de um contrato específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato encontrado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Contrato> getContratoById(
            @Parameter(description = "ID (UUID) do contrato a ser buscado.", required = true)
            @PathVariable UUID id) {

        return contratoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/responsavel/{responsavelId}")
    @Operation(summary = "Busca contratos de um responsável",
            description = "Retorna uma lista de todos os contratos associados a um ID de responsável específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Responsável não encontrado ou não possui contratos."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Contrato>> getContratosByResponsavel(
            @Parameter(description = "ID (UUID) do Responsável.", required = true)
            @PathVariable UUID responsavelId) {

        List<Contrato> contratos = contratoService.findByResponsavelId(responsavelId);
        return ResponseEntity.ok(contratos);
    }

    @GetMapping("/motorista/{motoristaId}")
    @Operation(summary = "Busca contratos de um motorista",
            description = "Retorna uma lista de todos os contratos associados a um ID de motorista específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado ou não possui contratos."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Contrato>> getContratosByMotorista(
            @Parameter(description = "ID (UUID) do Motorista.", required = true)
            @PathVariable UUID motoristaId) {

        List<Contrato> contratos = contratoService.findByMotoristaId(motoristaId);
        return ResponseEntity.ok(contratos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um contrato pelo ID (UUID)",
            description = "Atualiza os dados de um contrato existente (ex: mudança de status).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Contrato> updateContrato(
            @Parameter(description = "ID (UUID) do contrato a ser atualizado.", required = true)
            @PathVariable UUID id,

            @Valid @RequestBody Contrato contratoDetails) {

        // O GlobalExceptionHandler também captura os erros deste método
        return contratoService.update(id, contratoDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um contrato pelo ID (UUID)",
            description = "Remove o registro de um contrato do sistema (cancelamento).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contrato deletado com sucesso. (Sem conteúdo no retorno)"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Void> deleteContrato(
            @Parameter(description = "ID (UUID) do contrato a ser deletado.", required = true)
            @PathVariable UUID id) {

        if (contratoService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}