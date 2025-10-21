package br.edu.fiec.RotaVan.features.user.services;

// --- IMPORTAÇÕES ADICIONADAS ---
import br.edu.fiec.RotaVan.features.firebase.dto.FcmTokenRequest;
import java.util.UUID;
// --- FIM DAS IMPORTAÇÕES ---

import br.edu.fiec.RotaVan.features.user.dto.MyUserResponse;
import br.edu.fiec.RotaVan.features.user.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    void save(User user);
    MyUserResponse getMe(User user);

    // --- MÉTODO ADICIONADO ---
    User updateFcmToken(UUID userId, FcmTokenRequest request);
    // --- FIM DO MÉTODO ADICIONADO ---
}