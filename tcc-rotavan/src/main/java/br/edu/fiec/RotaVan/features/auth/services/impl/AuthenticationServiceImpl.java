package br.edu.fiec.RotaVan.features.auth.services.impl;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.CriancaRegisterDTO;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import br.edu.fiec.RotaVan.features.auth.services.AuthenticationService;
import br.edu.fiec.RotaVan.features.user.dto.EscolaRegisterRequest; // <-- IMPORTADO
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
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
    private final MotoristasRepository motoristasRepository;
    private final ResponsaveisRepository responsaveisRepository;


    public AuthenticationServiceImpl(UserRepository userRepository, EscolasRepository escolasRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, MotoristasRepository motoristasRepository, ResponsaveisRepository responsaveisRepository) {
        this.userRepository = userRepository;
        this.escolasRepository = escolasRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.motoristasRepository = motoristasRepository;
        this.responsaveisRepository = responsaveisRepository;
    }

    // --- INÍCIO DA REATORAÇÃO (Passo 4) ---

    /**
     * Método helper privado para criar um objeto User básico.
     * Centraliza a lógica de criação de User (nome, email, senha criptografada, role).
     */
    private User criarUserBase(String nome, String email, String senhaPura, User.Role role) {
        User user = new User();
        user.setNome(nome);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(senhaPura));
        user.setRole(role);
        return user;
    }

    @Override
    @Transactional
    public LoginResponse registerResponsavel(RegisterRequest request) {
        // 1. Criar o User (usando o helper)
        User user = criarUserBase(
                request.getNomeResponsavel(),
                request.getEmail(),
                request.getPassword(),
                User.Role.ROLE_RESPONSAVEL
        );

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
        responsavelProfile.setUser(user);

        // 5. Salvar o User (o perfil e as crianças serão salvas em cascata)
        userRepository.save(user);
        responsaveisRepository.save(responsavelProfile);

        // 6. Gerar e retornar o token
        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    @Override
    @Transactional
    public LoginResponse registerMotorista(MotoristaRegisterRequest request) {
        // 1. Criar o User com a role de Motorista (usando o helper)
        User user = criarUserBase(
                request.getNomeMotorista(),
                request.getEmail(),
                request.getPassword(),
                User.Role.ROLE_MOTORISTA
        );

        // 2. Criar o Perfil do Motorista
        Motoristas motoristaProfile = new Motoristas();
        motoristaProfile.setNomeMotorista(request.getNomeMotorista());
        motoristaProfile.setCnh(request.getCnh());
        motoristaProfile.setCpf(request.getCpf());
        motoristaProfile.setValCnh(request.getValCnh());

        // 3. Ligar o User ao Perfil
        motoristaProfile.setUser(user);

        // 4. Salvar o User (o perfil será salvo em cascata)
        userRepository.save(user);
        motoristasRepository.save(motoristaProfile); // Salva explicitamente

        // 5. Gerar e retornar o token
        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    @Override
    @Transactional
    public LoginResponse registerAdmin(AdminRegisterRequest request) {
        // 1. Criar o User com a role de Admin (usando o helper)
        User user = criarUserBase(
                request.getNome(),
                request.getEmail(),
                request.getPassword(),
                User.Role.ROLE_ADMIN
        );

        // 2. Salvar o User (não há perfil extra para o admin)
        userRepository.save(user);

        // 3. Gerar e retornar o token
        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    // --- MÉTODO NOVO ADICIONADO (PASSO 5) ---
    @Override
    @Transactional
    public LoginResponse registerEscola(EscolaRegisterRequest request) {
        // 1. Criar o User com a role de Escola
        User user = criarUserBase(
                request.getNome(), // O nome do User será o nome da Escola
                request.getEmail(),
                request.getSenha(),
                User.Role.ROLE_ESCOLA // Usando a nova Role
        );

        // 2. Criar o Perfil da Escola
        Escolas escolaProfile = new Escolas();
        escolaProfile.setNome(request.getNome());
        escolaProfile.setCnpj(request.getCnpj());
        escolaProfile.setEndereco(request.getEndereco());
//        escolaProfile.setTelefone(request.getTelefone());

        // 3. Ligar o perfil ao User
        escolaProfile.setUser(user);

        // 4. Salvar o User e o Perfil
        userRepository.save(user);
        escolasRepository.save(escolaProfile); // Salva o perfil da escola explicitamente

        // 5. Gerar e retornar o token
        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }
    // --- FIM DA ADIÇÃO ---

    @Override
    public LoginResponse register(RegisterRequest request) {
        return null;
    }

    // --- FIM DA REATORAÇÃO ---

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

}