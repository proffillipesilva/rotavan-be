package br.edu.fiec.RotaVan.features.auth.controllers;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import br.edu.fiec.RotaVan.features.auth.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/api/auth") // Define o caminho base para todos os endpoints de autenticação
@Tag(name = "1. Autenticação", description = "Endpoints para Login e Registro de novos usuários")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Registro de novo Responsável",
            description = "Cria um novo usuário com o perfil 'Responsável' e retorna um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Responsável registrado com sucesso. Retorna o token de login."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: email mal formatado, senha curta, campos obrigatórios faltando)."),
            @ApiResponse(responseCode = "409", description = "Conflito. O email ou CPF fornecido já está em uso."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Recebida requisição para registar novo responsável com email: {}", request.getEmail());
        LoginResponse response = authenticationService.register(request);
        log.info("Responsável registado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Login do Usuário",
            description = "Autentica um usuário (qualquer perfil) com email e senha, retornando um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido. Retorna o token de login."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (email ou senha incorretos)."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("Recebida requisição de login para o email: {}", request.getEmail());
        LoginResponse response = authenticationService.login(request);
        log.info("Login bem-sucedido para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    // ENDPOINT ADICIONADO PARA MOTORISTA
    @Operation(summary = "Registro de novo Motorista",
            description = "Cria um novo usuário com o perfil 'Motorista' e retorna um token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Motorista registrado com sucesso. Retorna o token de login."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: CNH faltando, email mal formatado)."),
            @ApiResponse(responseCode = "409", description = "Conflito. O email, CPF ou CNH já está em uso."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping("/register/motorista")
    public ResponseEntity<LoginResponse> registerMotorista(@Valid @RequestBody MotoristaRegisterRequest request) {
        log.info("Recebida requisição para registar novo motorista com email: {}", request.getEmail());
        LoginResponse response = authenticationService.registerMotorista(request);
        log.info("Motorista registado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    // ENDPOINT ADICIONADO PARA ADMIN
    @Operation(summary = "Registro de novo Admin",
            description = "Cria um novo usuário com o perfil 'Admin'. Endpoint de setup inicial do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin registrado com sucesso. Retorna o token de login."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "409", description = "Conflito. O email já está em uso."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping("/register/admin")
    public ResponseEntity<LoginResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        log.info("Recebida requisição para registar novo admin com email: {}", request.getEmail());
        LoginResponse response = authenticationService.registerAdmin(request);
        log.info("Admin registado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }
}