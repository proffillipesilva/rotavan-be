package br.edu.fiec.RotaVan.features.auth.controllers;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import br.edu.fiec.RotaVan.features.auth.services.AuthenticationService;
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
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register/responsavel") // <-- CORRIGIDO
    public ResponseEntity<LoginResponse> registerResponsavel(@Valid @RequestBody RegisterRequest request) { // <-- CORRIGIDO
        log.info("Recebida requisição para registar novo responsável com email: {}", request.getEmail());
        LoginResponse response = authenticationService.registerResponsavel(request); // <-- CORRIGIDO
        log.info("Responsável registado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        log.info("Recebida requisição de login para o email: {}", request.getEmail());
        LoginResponse response = authenticationService.login(request);
        log.info("Login bem-sucedido para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    // ENDPOINT ADICIONADO PARA MOTORISTA
    @PostMapping("/register/motorista")
    public ResponseEntity<LoginResponse> registerMotorista(@Valid @RequestBody MotoristaRegisterRequest request) {
        log.info("Recebida requisição para registar novo motorista com email: {}", request.getEmail());
        LoginResponse response = authenticationService.registerMotorista(request);
        log.info("Motorista registado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    // ENDPOINT ADICIONADO PARA ADMIN
    @PostMapping("/register/admin")
    public ResponseEntity<LoginResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        log.info("Recebida requisição para registar novo admin com email: {}", request.getEmail());
        LoginResponse response = authenticationService.registerAdmin(request);
        log.info("Admin registado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }
}