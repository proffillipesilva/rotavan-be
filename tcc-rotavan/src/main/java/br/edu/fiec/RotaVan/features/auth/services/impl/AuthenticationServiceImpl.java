package br.edu.fiec.RotaVan.features.auth.services.impl;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.CriancaRegisterDTO;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import br.edu.fiec.RotaVan.features.auth.services.AuthenticationService;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
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
        user.setNome(request.getNomeResponsavel()); // Preenche o novo campo 'nome'
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
                crianca.setResponsavel(responsavelProfile);
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
        String token = jwtService.generateTokenComplete(user);
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

        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    // --- MÉTODO MODIFICADO ---
    @Override
    @Transactional
    public LoginResponse registerMotorista(MotoristaRegisterRequest request) {
        // 1. Criar o User com a role de Motorista
        User user = new User();
        user.setNome(request.getNomeMotorista());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.ROLE_MOTORISTA);

        // 2. Criar o Perfil do Motorista
        Motoristas motoristaProfile = new Motoristas();
        motoristaProfile.setNomeMotorista(request.getNomeMotorista());
        motoristaProfile.setCnh(request.getCnh()); // Tipo String
        motoristaProfile.setCpf(request.getCpf()); // Tipo String
        motoristaProfile.setValCnh(request.getValCnh()); // --- LINHA ADICIONADA ---

        // 3. Ligar o User ao Perfil
        user.setMotoristaProfile(motoristaProfile);
        motoristaProfile.setUser(user);

        // 4. Salvar o User (o perfil será salvo em cascata)
        userRepository.save(user);

        // 5. Gerar e retornar o token
        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }
    // --- FIM DA MODIFICAÇÃO ---

    @Override
    @Transactional
    public LoginResponse registerAdmin(AdminRegisterRequest request) {
        // 1. Criar o User com a role de Admin
        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.ROLE_ADMIN);

        // 2. Salvar o User (não há perfil extra para o admin)
        userRepository.save(user);

        // 3. Gerar e retornar o token
        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }
}