package br.edu.fiec.RotaVan.features.user.controllers; // Verifique se o pacote está correto

import br.edu.fiec.RotaVan.features.user.dto.CriancaDTO;
import br.edu.fiec.RotaVan.features.user.dto.MyUserResponse;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.services.ResponsaveisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

// import java.util.ArrayList; // <-- Não é mais necessário
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/responsaveis")
@Tag(name = "Responsáveis", description = "API para gerenciamento de Responsáveis e seus dependentes")
public class ResponsaveisController {

    private final ResponsaveisService responsaveisService;

    public ResponsaveisController(ResponsaveisService responsaveisService) {
        this.responsaveisService = responsaveisService;
    }

    @Operation(summary = "Criação do usuário Responsável", description = "Cria as informações do usuário na API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Responsável criado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, verifique os campos obrigatórios."),
            @ApiResponse(responseCode = "409", description = "Conflito. O email ou CPF fornecido já está em uso."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<Responsaveis> createResponsavel(@RequestBody Responsaveis responsavel) {
        // NOTA: Este endpoint parece ser legado (pré-DTOs). O registo real
        // acontece no AuthController (/auth/register/responsavel).
        Responsaveis savedResponsavel = responsaveisService.criaResponsavel(responsavel);
        return new ResponseEntity<>(savedResponsavel, HttpStatus.CREATED);
    }


    @Operation(summary = "Adiciona um novo dependente a um responsável",
            description = "Cria um novo registro de dependente e o associa a um responsável existente, usando o ID do responsável.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "dependente criado e associado com sucesso!"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para o dependente."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Responsável não encontrado com o ID (UUID) fornecido."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{responsavelId}/criancas")
    public ResponseEntity<Crianca> adicionarCrianca(

            @Parameter(description = "ID (UUID) do responsável que receberá o dependente.", required = true)
            @PathVariable UUID responsavelId,

            @RequestBody Crianca novaCrianca) {

        return responsaveisService.adicionarCrianca(responsavelId, novaCrianca)
                .map(crianca -> new ResponseEntity<>(crianca, HttpStatus.CREATED))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lista todos os responsáveis",
            description = "Retorna uma lista de todos os usuários com o perfil de 'Responsável'. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de responsáveis retornada com sucesso (pode ser uma lista vazia)."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<List<Responsaveis>> getAllResponsaveis() {
        return ResponseEntity.ok(responsaveisService.findAll());
    }

    @Operation(summary = "Busca um responsável pelo ID (UUID)",
            description = "Retorna os dados de um responsável específico baseado no seu UUID. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsável encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Responsável não encontrado para o ID fornecido."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<Responsaveis> getResponsavelById(
            @Parameter(description = "ID (UUID) do responsável a ser buscado.", required = true)
            @PathVariable UUID id) {
        return responsaveisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualiza um responsável pelo ID (UUID)",
            description = "Atualiza os dados de um responsável existente. O ID é fornecido na URL e os novos dados no corpo da requisição. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsável atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição (ex: campos obrigatórios nulos)."),
            @ApiResponse(responseCode = "404", description = "Responsável não encontrado para o ID fornecido (não foi possível atualizar)."),
            @ApiResponse(responseCode = "409", description = "Conflito (ex: tentando usar um email que já pertence a outro usuário)."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<Responsaveis> updateResponsavel(
            @Parameter(description = "ID (UUID) do responsável a ser atualizado.", required = true)
            @PathVariable UUID id, @RequestBody Responsaveis responsavelDetails) {
        return responsaveisService.update(id, responsavelDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // --- MÉTODO CORRIGIDO ---
    @Operation(summary = "Lista os dependentes do responsável logado",
            description = "Retorna uma lista de todos os dependentes (crianças) associados ao responsável que está autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de dependentes retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "404", description = "Perfil de responsável não encontrado para o usuário logado."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/dependentes")
    public ResponseEntity<List<CriancaDTO>> getDependentes(Authentication authentication) {
        // 1. Obtém o objeto 'User' do utilizador que fez a requisição.
        User user = (User) authentication.getPrincipal();

        // 2. Chama o serviço para buscar os dependentes REAIS
        List<CriancaDTO> criancaDTOS = responsaveisService.findDependentesByAuthUser(user);

        // 3. Retorna os dados com um status HTTP 200 OK.
        return ResponseEntity.ok(criancaDTOS);
    }
    // --- FIM DA CORREÇÃO ---

}