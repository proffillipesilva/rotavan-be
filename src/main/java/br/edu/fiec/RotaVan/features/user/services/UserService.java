package br.edu.fiec.RotaVan.features.user.services;


import br.edu.fiec.RotaVan.features.firebase.dto.FcmTokenRequest;
import java.util.UUID;


import br.edu.fiec.RotaVan.features.user.dto.MyUserResponse;
import br.edu.fiec.RotaVan.features.user.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    void save(User user);
    MyUserResponse getMe(User user);

    User updateFcmToken(UUID userId, FcmTokenRequest request);

}