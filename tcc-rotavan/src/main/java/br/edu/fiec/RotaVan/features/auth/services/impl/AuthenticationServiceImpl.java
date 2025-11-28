package br.edu.fiec.RotaVan.features.auth.services.impl;

import br.edu.fiec.RotaVan.features.auth.dto.AdminRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.CriancaRegisterDTO;
import br.edu.fiec.RotaVan.features.auth.dto.LoginRequest;
import br.edu.fiec.RotaVan.features.auth.dto.LoginResponse;
import br.edu.fiec.RotaVan.features.auth.dto.MotoristaRegisterRequest;
import br.edu.fiec.RotaVan.features.auth.dto.RegisterRequest;
import br.edu.fiec.RotaVan.features.auth.services.AuthenticationService;
import br.edu.fiec.RotaVan.features.user.dto.EscolaRegisterRequest;
import br.edu.fiec.RotaVan.features.user.models.Crianca;
import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.features.user.repositories.EscolasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.UserRepository;
import br.edu.fiec.RotaVan.shared.service.GoogleMapsService;
import br.edu.fiec.RotaVan.utils.JwtService;
import com.google.maps.model.LatLng;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final UserRepository userRepository;
    private final EscolasRepository escolasRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MotoristasRepository motoristasRepository;
    private final ResponsaveisRepository responsaveisRepository;
    private final GoogleMapsService googleMapsService;

    public AuthenticationServiceImpl(
            UserRepository userRepository,
            EscolasRepository escolasRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            MotoristasRepository motoristasRepository,
            ResponsaveisRepository responsaveisRepository,
            GoogleMapsService googleMapsService) {
        this.userRepository = userRepository;
        this.escolasRepository = escolasRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.motoristasRepository = motoristasRepository;
        this.responsaveisRepository = responsaveisRepository;
        this.googleMapsService = googleMapsService;
    }

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
        // 1. Criar o User
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

        // --- NOVO: Geocoding para o Responsável ---
        // Verifica se tem endereço e tenta buscar coordenadas
        if (request.getEnderecoCasa() != null && !request.getEnderecoCasa().isEmpty()) {
            try {
                String busca = request.getEnderecoCasa();
                // Adiciona cidade se não tiver, para ajudar a precisão
                if (!busca.toLowerCase().contains("indaiatuba")) {
                    busca += ", Indaiatuba - SP";
                }

                LatLng coords = googleMapsService.buscarCoordenadas(busca);
                if (coords != null) {
                    responsavelProfile.setLatitude(BigDecimal.valueOf(coords.lat));
                    responsavelProfile.setLongitude(BigDecimal.valueOf(coords.lng));
                    log.info("Coordenadas obtidas para o responsável {}: {}, {}", request.getNomeResponsavel(), coords.lat, coords.lng);
                }
            } catch (Exception e) {
                log.error("Erro ao buscar coordenadas do responsável: {}", e.getMessage());
            }
        }
        // ------------------------------------------

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
                crianca.setDataNascimento(dto.getDataNascimento());
                crianca.setNivelEscolar(dto.getNivelEscolar());
                crianca.setEndereco(dto.getEndereco());
                crianca.setTipoServico(dto.getTipoServico());
                crianca.setPeriodo(dto.getPeriodo());

                // Lógica de Geocoding da Criança (já existia)
                if (dto.getLatitude() != null && dto.getLongitude() != null) {
                    crianca.setLatitude(dto.getLatitude());
                    crianca.setLongitude(dto.getLongitude());
                } else if (dto.getEndereco() != null && !dto.getEndereco().isEmpty()) {
                    try {
                        LatLng coords = googleMapsService.buscarCoordenadas(dto.getEndereco());
                        if (coords != null) {
                            crianca.setLatitude(BigDecimal.valueOf(coords.lat));
                            crianca.setLongitude(BigDecimal.valueOf(coords.lng));
                        }
                    } catch (Exception e) {
                        log.error("Erro ao buscar coordenadas para criança: {}", e.getMessage());
                    }
                }

                criancasList.add(crianca);
            }
        }
        responsavelProfile.setCriancas(criancasList);

        // 4. Ligar o User ao Perfil e Salvar
        responsavelProfile.setUser(user);
        userRepository.save(user);
        responsaveisRepository.save(responsavelProfile);

        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    @Override
    @Transactional
    public LoginResponse registerMotorista(MotoristaRegisterRequest request) {
        // 1. Criar o User
        User user = criarUserBase(
                request.getNomeMotorista(),
                request.getEmail(),
                request.getPassword(),
                User.Role.ROLE_MOTORISTA
        );

        // --- NOVO: Define o endereço no User e faz Geocoding ---
        user.setEndereco(request.getEndereco());

        if (request.getEndereco() != null && !request.getEndereco().isEmpty()) {
            try {
                String busca = request.getEndereco();
                if (!busca.toLowerCase().contains("indaiatuba")) {
                    busca += ", Indaiatuba - SP";
                }

                LatLng coords = googleMapsService.buscarCoordenadas(busca);
                if (coords != null) {
                    user.setLatitude(BigDecimal.valueOf(coords.lat));
                    user.setLongitude(BigDecimal.valueOf(coords.lng));
                    log.info("Coordenadas obtidas para o motorista {}: {}, {}", request.getNomeMotorista(), coords.lat, coords.lng);
                }
            } catch (Exception e) {
                log.error("Erro ao buscar coordenadas do motorista: {}", e.getMessage());
            }
        }
        // ------------------------------------------------------

        // 2. Criar o Perfil do Motorista
        Motoristas motoristaProfile = new Motoristas();
        motoristaProfile.setNomeMotorista(request.getNomeMotorista());
        motoristaProfile.setCnh(request.getCnh());
        motoristaProfile.setCpf(request.getCpf());
        motoristaProfile.setValCnh(request.getValCnh());
        // motoristaProfile.setVeiculos(...) -> Veículos geralmente são cadastrados num endpoint separado ou lista

        // 3. Ligar o User ao Perfil e Salvar
        motoristaProfile.setUser(user);
        userRepository.save(user);
        motoristasRepository.save(motoristaProfile);

        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    @Override
    @Transactional
    public LoginResponse registerAdmin(AdminRegisterRequest request) {
        User user = criarUserBase(
                request.getNome(),
                request.getEmail(),
                request.getPassword(),
                User.Role.ROLE_ADMIN
        );
        userRepository.save(user);
        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    @Override
    @Transactional
    public LoginResponse registerEscola(EscolaRegisterRequest request) {
        User user = criarUserBase(
                request.getNome(),
                request.getEmail(),
                request.getSenha(),
                User.Role.ROLE_ESCOLA
        );

        Escolas escolaProfile = new Escolas();
        escolaProfile.setNome(request.getNome());
        escolaProfile.setCnpj(request.getCnpj());
        escolaProfile.setEndereco(request.getEndereco());

        // Se quiser geocoding para escola no registo manual também:
        if (request.getEndereco() != null) {
            try {
                LatLng coords = googleMapsService.buscarCoordenadas(request.getEndereco());
                if (coords != null) {
                    escolaProfile.setLatitude(BigDecimal.valueOf(coords.lat));
                    escolaProfile.setLongitude(BigDecimal.valueOf(coords.lng));
                }
            } catch (Exception e) { /* log */ }
        }

        escolaProfile.setUser(user);
        userRepository.save(user);
        escolasRepository.save(escolaProfile);

        String token = jwtService.generateTokenComplete(user);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        return null;
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
}