package br.edu.fiec.RotaVan.features.user.controllers;

import br.edu.fiec.RotaVan.features.user.dto.MyUserResponse;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.services.UserService;
// import br.edu.fiec.RotaVan.utils.ImageUtils; // Não precisamos mais disso aqui
import br.edu.fiec.RotaVan.shared.service.S3Service; // IMPORTAR S3
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException; // IMPORTAR

@RestController
@RequestMapping("/v1/api/users")
@Tag(name = "User", description = "API para gerenciamento de usuários")
public class UserController {

    private final UserService userService;
    private final S3Service s3Service; // ADICIONAR

    // CONSTRUTOR ATUALIZADO
    public UserController(UserService userService, S3Service s3Service) {
        this.userService = userService;
        this.s3Service = s3Service; // ADICIONAR
    }
    // ANOTAÇÃO SWAGGER
    @Operation(summary = "Atualiza a foto de perfil do usuário logado",
            description = "Este endpoint recebe um arquivo de imagem (Multipart/form-data). " +
                    "Ele faz o upload da imagem para o S3 e atualiza o campo 'picture' do usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nenhum arquivo enviado ou formato inválido"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao processar o upload da imagem")
    })
    @PutMapping("/perfil/foto")
    // Adicionar "throws IOException"
    public ResponseEntity<Void> inserirFoto(@RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();

        // LINHA ANTIGA:
        // String nomeDaImagem = ImageUtils.saveImage(image);

        // LINHA NOVA:
        String nomeDaImagem = s3Service.uploadFile(image); // Salva no S3

        user.setPicture(nomeDaImagem); // Salva a chave S3 (ou URL) no usuário
        userService.save(user);

        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para obter as informações do perfil do utilizador autenticado.
     * @param authentication O objeto de autenticação injetado pelo Spring Security.
     * @return Um ResponseEntity contendo os dados do perfil do utilizador.
     */
    // ANOTAÇÃO SWAGGER
    @Operation(summary = "Busca os dados do usuário logado",
            description = "Retorna as informações de perfil (como nome, email, etc.) do usuário que está fazendo a requisição. Este endpoint é protegido e requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados do usuário retornados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado (Token JWT inválido ou não fornecido)"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<MyUserResponse> getMe(Authentication authentication) {
        // 1. Obtém o objeto 'User' do utilizador que fez a requisição.
        User user = (User) authentication.getPrincipal();

        // 2. Chama o serviço para montar a resposta com os dados do perfil.
        MyUserResponse response = userService.getMe(user);

        // 3. Retorna os dados com um status HTTP 200 OK.
        return ResponseEntity.ok(response);
    }
}