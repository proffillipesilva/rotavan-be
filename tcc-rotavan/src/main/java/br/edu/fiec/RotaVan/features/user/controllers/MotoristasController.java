package br.edu.fiec.RotaVan.features.user.controllers;

import br.edu.fiec.RotaVan.features.user.dto.MotoristaResponseDTO;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.services.MotoristasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/motoristas") // Define a rota base para este controller
@RequiredArgsConstructor // Injeta o service via construtor (boa prática)
@Tag(name = "Motoristas", description = "Endpoints para consultar dados de motoristas")
public class MotoristasController {

    // Injeção do serviço
    private final MotoristasService motoristasService;

    /**
     * Endpoint para listar TODOS os motoristas.
     * @return Uma lista de MotoristaResponseDTO, segura para o cliente.
     */
    @Operation(summary = "Lista todos os motoristas cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de motoristas retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<MotoristaResponseDTO>> getAllMotoristas() {
        // 1. Busca a lista de entidades 'Motoristas' do banco
        List<Motoristas> motoristas = motoristasService.findAll(); // (Suposição do nome do método)

        // 2. Converte (mapeia) a lista de entidades para uma lista de DTOs
        List<MotoristaResponseDTO> responseList = motoristas.stream()
                .map(MotoristaResponseDTO::fromEntity) // Usa o método estático que criámos
                .collect(Collectors.toList());

        // 3. Retorna a lista de DTOs
        return ResponseEntity.ok(responseList);
    }

    /**
     * Endpoint para buscar um motorista específico pelo seu ID (UUID)
     * @param id O UUID do motorista (ID do perfil 'Motoristas')
     * @return Um único MotoristaResponseDTO
     */
    @Operation(summary = "Busca um motorista pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Motorista encontrado"),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MotoristaResponseDTO> getMotoristaById(@PathVariable UUID id) {
        // 1. Busca a entidade 'Motoristas' (o serviço deve lançar erro se não encontrar)
        Motoristas motorista = motoristasService.findById(id); // (Suposição do nome do método)

        // 2. Converte a entidade para o DTO de resposta
        MotoristaResponseDTO responseDto = MotoristaResponseDTO.fromEntity(motorista);

        // 3. Retorna o DTO
        return ResponseEntity.ok(responseDto);
    }

    // Outros métodos (como PUT para atualizar, DELETE para excluir)
    // seguiriam a mesma lógica:
    // - O método DELETE não retorna corpo, então não muda.
    // - O método PUT (atualizar) receberia um DTO de Requisição,
    //   chamaria o service, e retornaria um DTO de Resposta (igual ao getById).
}