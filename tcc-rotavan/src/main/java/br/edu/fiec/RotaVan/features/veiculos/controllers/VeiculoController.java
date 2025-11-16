package br.edu.fiec.RotaVan.features.veiculos.controllers; // Ajuste o pacote

import br.edu.fiec.RotaVan.features.veiculos.models.Veiculo;
import br.edu.fiec.RotaVan.features.veiculos.services.VeiculoService;
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
@RequestMapping("/v1/api/veiculos") // Caminho base
@AllArgsConstructor
@Tag(name = "Veículos", description = "API para gerenciamento de Veículos")
@SecurityRequirement(name = "bearerAuth")
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping
    @Operation(summary = "Cria (cadastra) um novo veículo",
            description = "Registra um novo veículo no sistema e o associa a um motorista.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: placa duplicada, formato inválido ou motorista não encontrado)."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Motorista associado não encontrado (RuntimeException)."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Veiculo> createVeiculo(@Valid @RequestBody Veiculo veiculo) {
        try {
            Veiculo savedVeiculo = veiculoService.save(veiculo);
            return new ResponseEntity<>(savedVeiculo, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) { // Captura erro de placa duplicada
            return ResponseEntity.badRequest().body(null); // Ou retornar uma mensagem de erro
        } catch (RuntimeException e) { // Captura erro se motorista não existir
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Ou retornar mensagem
        }
    }

    @GetMapping
    @Operation(summary = "Lista todos os veículos",
            description = "Retorna uma lista de todos os veículos cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<List<Veiculo>> getAllVeiculos() {
        return ResponseEntity.ok(veiculoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um veículo pelo ID (UUID)",
            description = "Retorna os dados de um veículo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Veiculo> getVeiculoById(
            @Parameter(description = "ID (UUID) do veículo a ser buscado.", required = true)
            @PathVariable UUID id) {

        return veiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/placa/{placa}") // Exemplo de busca por placa
    @Operation(summary = "Busca um veículo pela placa",
            description = "Retorna os dados de um veículo específico com base na placa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado para a placa fornecida."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Veiculo> getVeiculoByPlaca(
            @Parameter(description = "Placa do veículo (ex: BRA2E19).", required = true)
            @PathVariable String placa) {

        return veiculoService.findByPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um veículo pelo ID (UUID)",
            description = "Atualiza os dados de um veículo existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: placa duplicada)."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Veículo ou Motorista associado não encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Veiculo> updateVeiculo(
            @Parameter(description = "ID (UUID) do veículo a ser atualizado.", required = true)
            @PathVariable UUID id,

            @Valid @RequestBody Veiculo veiculoDetails) {

        try {
            return veiculoService.update(id, veiculoDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um veículo pelo ID (UUID)",
            description = "Remove o registro de um veículo do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Veículo deletado com sucesso. (Sem conteúdo no retorno)"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    public ResponseEntity<Void> deleteVeiculo(
            @Parameter(description = "ID (UUID) do veículo a ser deletado.", required = true)
            @PathVariable UUID id) {

        if (veiculoService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Poderia adicionar um endpoint para listar veículos por motorista ID
    // GET /v1/api/veiculos/motorista/{motoristaId}
}