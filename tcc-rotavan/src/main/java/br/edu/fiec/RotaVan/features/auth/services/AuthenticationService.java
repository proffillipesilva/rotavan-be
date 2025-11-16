package br.edu.fiec.RotaVan.features.auth.services;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import br.edu.fiec.RotaVan.features.user.dto.EscolaRegisterRequest; // <-- 1. IMPORTAR
import jakarta.validation.Valid;

public interface AuthenticationService {
    LoginResponse registerResponsavel(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse registerMotorista(MotoristaRegisterRequest request);
    LoginResponse registerAdmin(AdminRegisterRequest request);
    LoginResponse registerEscola(EscolaRegisterRequest request); // <-- 2. ADICIONAR ESTA LINHA

    LoginResponse register(@Valid RegisterRequest request);
}