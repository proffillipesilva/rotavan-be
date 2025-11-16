package br.edu.fiec.RotaVan.features.user.services.impl;

// --- IMPORTAÇÕES ADICIONADAS ---
import br.edu.fiec.RotaVan.features.firebase.dto.FcmTokenRequest;
import java.util.UUID;
// --- FIM DAS IMPORTAÇÕES ---

import br.edu.fiec.RotaVan.features.user.dto.MyUserResponse;
import br.edu.fiec.RotaVan.features.user.models.Motoristas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.repositories.CriancaRepository;
import br.edu.fiec.RotaVan.features.user.repositories.MotoristasRepository;
import br.edu.fiec.RotaVan.features.user.repositories.ResponsaveisRepository;
import br.edu.fiec.RotaVan.features.user.repositories.UserRepository;
import br.edu.fiec.RotaVan.features.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MotoristasRepository motoristasRepository;
    private final CriancaRepository criancaRepository;
    private final ResponsaveisRepository responsaveisRepository;


    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
            }
        };
    }


    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public MyUserResponse getMe(User user) {
        MyUserResponse response = new MyUserResponse();
        response.setUserId(user.getId());
        response.setNome(user.getNome());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setPicture(user.getPicture());

        switch (user.getRole()) {
            case ROLE_RESPONSAVEL:
                Responsaveis responsavel = responsaveisRepository.findByUser(user).orElseThrow();
                if (responsavel != null) {
                    response.setCpfResponsavel(responsavel.getCpfResponsavel());
                    response.setEnderecoCasa(responsavel.getEnderecoCasa());
                }
                break;
            case ROLE_MOTORISTA:
                Motoristas motorista = motoristasRepository.findByUser(user).orElseThrow();
                if (motorista != null) {
                    response.setCnh(motorista.getCnh());
                    response.setCpfMotorista(motorista.getCpf());
                }
                break;
            case ROLE_ADMIN:
                // Para o admin, os dados básicos que já preenchemos são suficientes.
                // Não há ações adicionais necessárias.
                break;
        }

        return response;
    }

    // --- IMPLEMENTAÇÃO ADICIONADA ---
    @Override // Adicione @Override se estiver a implementar a partir da interface
    public User updateFcmToken(UUID userId, FcmTokenRequest request) {
        // 1. Busca o utilizador pelo ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

        // 2. Atualiza o atributo fcmToken
        user.setFcmToken(request.getFcmToken());

        // 3. Salva a alteração no banco de dados
        return userRepository.save(user);
    }
    // --- FIM DA IMPLEMENTAÇÃO ---
}