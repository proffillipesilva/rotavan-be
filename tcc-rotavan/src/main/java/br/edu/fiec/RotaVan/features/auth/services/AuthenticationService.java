package br.edu.fiec.RotaVan.features.auth.services;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;

public interface AuthenticationService {
    LoginResponse registerResponsavel(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    // Declaração do método para registar motorista (que já tínhamos planeado)
    LoginResponse registerMotorista(MotoristaRegisterRequest request);

    // MÉTODO ADICIONADO
    LoginResponse registerAdmin(AdminRegisterRequest request);
}