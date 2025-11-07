package br.edu.fiec.RotaVan.features.user.controllers;

import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.services.MotoristasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/motoristas") // URL base para todos os endpoints de motoristas
public class MotoristasController {

    private final MotoristasService motoristasService;

    public MotoristasController(MotoristasService motoristasService) {
        this.motoristasService = motoristasService;
    }

    @Operation(summary = "Cria um novo motorista",
            description = "Cadastra um novo motorista no sistema. Não requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Motorista criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos. Verifique os campos obrigatórios (ex: nome, cnh, email)."),
            @ApiResponse(responseCode = "409", description = "Conflito. O email, CPF ou CNH fornecido já está em uso."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<Motoristas> createMotorista(@RequestBody Motoristas motorista) {
        Motoristas savedMotorista = motoristasService.save(motorista);
        return new ResponseEntity<>(savedMotorista, HttpStatus.CREATED);
    }

    @Operation(summary = "Lista todos os motoristas",
            description = "Retorna uma lista de todos os motoristas cadastrados. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de motoristas retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<Motoristas>> getAllMotoristas() {
        List<Motoristas> motoristas = motoristasService.findAll();
        return ResponseEntity.ok(motoristas);
    }

    @Operation(summary = "Busca um motorista pelo ID (UUID)",
            description = "Retorna os dados de um motorista específico. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Motorista encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<Motoristas> getMotoristaById(
            @Parameter(description = "ID (UUID) do motorista a ser buscado.", required = true)
            @PathVariable UUID id) {
        return motoristasService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualiza um motorista pelo ID (UUID)",
            description = "Atualiza os dados de um motorista existente. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Motorista atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição (ex: campos obrigatórios nulos)."),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "409", description = "Conflito (ex: email/CPF/CNH já pertence a outro usuário)."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<Motoristas> updateMotorista(
            @Parameter(description = "ID (UUID) do motorista a ser atualizado.", required = true)
            @PathVariable UUID id, @RequestBody Motoristas motoristaDetails) {
        return motoristasService.update(id, motoristaDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deleta um motorista pelo ID (UUID)",
            description = "Remove o registro de um motorista do sistema. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Motorista deletado com sucesso. (Não há conteúdo no corpo da resposta)"),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMotorista(
            @Parameter(description = "ID (UUID) do motorista a ser deletado.", required = true)
            @PathVariable UUID id) {
        if (motoristasService.deleteById(id)) {
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}