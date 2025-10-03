package br.edu.fiec.RotaVan.features.user.controllers;

import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.services.UserService;
import br.edu.fiec.RotaVan.utils.ImageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para fazer o upload de uma foto de perfil para o utilizador autenticado.
     * @param image O ficheiro da imagem enviado na requisição.
     * @param authentication O objeto de autenticação injetado pelo Spring Security, contendo os dados do utilizador logado.
     * @return Uma resposta HTTP 200 OK se o upload for bem-sucedido.
     */
    @PutMapping("/perfil/foto")
    public ResponseEntity<Void> inserirFoto(@RequestParam("image") MultipartFile image, Authentication authentication) {
        // 1. Obtém o objeto 'User' do utilizador atualmente autenticado.
        User user = (User) authentication.getPrincipal();

        // 2. Chama a classe utilitária para salvar a imagem no servidor.
        //    (Lembre-se de criar a classe ImageUtils e adicionar a dependência 'thumbnailator' ao pom.xml)
        String nomeDaImagem = ImageUtils.saveImage(image);

        // 3. Atualiza o campo 'picture' no objeto do utilizador.
        user.setPicture(nomeDaImagem);

        // 4. Salva o objeto 'User' atualizado de volta na base de dados.
        userService.save(user);

        // 5. Retorna uma resposta de sucesso.
        return ResponseEntity.ok().build();
    }
}