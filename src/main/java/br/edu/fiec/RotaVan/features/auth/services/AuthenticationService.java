package br.edu.fiec.RotaVan.features.auth.services;

import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;

public interface AuthenticationService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}