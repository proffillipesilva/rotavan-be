package br.edu.fiec.RotaVan.features.user.services;// ...
import br.edu.fiec.RotaVan.features.user.dto.MyUserResponse;
import br.edu.fiec.RotaVan.features.user.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {
    UserDetailsService userDetailsService();
    void save(User user);
    MyUserResponse getMe(User user);
}