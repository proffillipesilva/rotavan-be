package br.edu.fiec.RotaVan.config;

import br.edu.fiec.RotaVan.features.user.repositories.UserRepository;
import com.google.maps.GeoApiContext; // <-- IMPORTAÇÃO ADICIONADA
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // <-- IMPORTAÇÃO ADICIONADA
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    // --- INÍCIO DA ADIÇÃO (Google Maps) ---
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    /**
     * Cria um Bean singleton do GeoApiContext, que é o cliente principal
     * para fazer chamadas à API do Google Maps.
     */
    @Bean
    public GeoApiContext geoApiContext() {
        // A API Key é lida do seu application.properties
        return new GeoApiContext.Builder()
                .apiKey(googleMapsApiKey)
                .build();
    }
    // --- FIM DA ADIÇÃO (Google Maps) ---


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}