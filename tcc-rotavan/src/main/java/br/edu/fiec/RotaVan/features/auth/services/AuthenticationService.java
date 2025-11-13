package br.edu.fiec.RotaVan.features.auth.services;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import jakarta.validation.Valid;

public interface AuthenticationService {
    LoginResponse registerResponsavel(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse registerMotorista(MotoristaRegisterRequest request);
    LoginResponse registerAdmin(AdminRegisterRequest request);

    LoginResponse register(@Valid RegisterRequest request);
}