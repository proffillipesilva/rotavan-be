package br.edu.fiec.RotaVan.features.auth.controllers;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import br.edu.fiec.RotaVan.features.auth.services.AuthenticationService;
import br.edu.fiec.RotaVan.features.user.dto.EscolaRegisterRequest; // <-- 1. IMPORTAR
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register/responsavel")
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Recebida requisição para registar novo responsável com email: {}", request.getEmail());
        LoginResponse response = authenticationService.register(request);
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


    @PostMapping("/register/motorista")
    public ResponseEntity<LoginResponse> registerMotorista(@Valid @RequestBody MotoristaRegisterRequest request) {
        log.info("Recebida requisição para registar novo motorista com email: {}", request.getEmail());
        LoginResponse response = authenticationService.registerMotorista(request);
        log.info("Motorista registado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register/admin")
    public ResponseEntity<LoginResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        log.info("Recebida requisição para registar novo admin com email: {}", request.getEmail());
        LoginResponse response = authenticationService.registerAdmin(request);
        log.info("Admin registado com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    // --- 2. ADICIONAR ESTE NOVO ENDPOINT ---
    @PostMapping("/register/escola")
    public ResponseEntity<LoginResponse> registerEscola(@Valid @RequestBody EscolaRegisterRequest request) {
        log.info("Recebida requisição para registar nova escola com email: {}", request.getEmail());
        // Chama o novo método no service
        LoginResponse response = authenticationService.registerEscola(request);
        log.info("Escola registada com sucesso para o email: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }
    // --- FIM DA ADIÇÃO ---
}