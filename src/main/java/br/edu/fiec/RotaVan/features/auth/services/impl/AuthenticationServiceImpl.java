package br.edu.fiec.RotaVan.features.auth.services.impl;

import br.edu.fiec.RotaVan.features.auth.dto.CriancaRegisterDTO;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import br.edu.fiec.RotaVan.features.auth.services.AuthenticationService;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.repositories.EscolasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.UserRepository;
import br.edu.fiec.RotaVan.utils.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final EscolasRepository escolasRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(UserRepository userRepository, EscolasRepository escolasRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.escolasRepository = escolasRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // 1. Criar o User
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.ROLE_RESPONSAVEL);

        // 2. Criar o Perfil do Responsável
        Responsaveis responsavelProfile = new Responsaveis();
        responsavelProfile.setNomeResponsavel(request.getNomeResponsavel());
        responsavelProfile.setCpfResponsavel(request.getCpfResponsavel());
        responsavelProfile.setEnderecoCasa(request.getEnderecoCasa());

        // 3. Processar e criar as crianças
        List<Crianca> criancasList = new ArrayList<>();
        if (request.getCriancas() != null) {
            for (CriancaRegisterDTO dto : request.getCriancas()) {
                Escolas escola = escolasRepository.findById(dto.getEscolaId())
                        .orElseThrow(() -> new RuntimeException("Escola com id " + dto.getEscolaId() + " não encontrada."));

                Crianca crianca = new Crianca();
                crianca.setNome(dto.getNome());
                crianca.setEscola(escola);
                crianca.setResponsavel(responsavelProfile); // Liga a criança ao perfil do responsável
                criancasList.add(crianca);
            }
        }
        responsavelProfile.setCriancas(criancasList);

        // 4. Ligar o User ao Perfil
        user.setResponsavelProfile(responsavelProfile);
        responsavelProfile.setUser(user);

        // 5. Salvar o User (o perfil e as crianças serão salvas em cascata)
        userRepository.save(user);

        // 6. Gerar e retornar o token
        String token = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos."));

        String token = jwtService.generateToken(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }
}