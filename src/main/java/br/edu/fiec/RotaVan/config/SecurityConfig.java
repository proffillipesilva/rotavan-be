package br.edu.fiec.RotaVan.config;

import br.edu.fiec.RotaVan.config.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Remova estas importações se não estiverem mais em uso aqui
// import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource; // Mantenha esta
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Remova estas importações se não estiverem mais em uso aqui
// import java.util.Arrays;
// import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource; // <-- ADICIONE ESTE CAMPO

    // --- CONSTRUTOR MODIFICADO ---
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider,
            CorsConfigurationSource corsConfigurationSource // <-- ADICIONE ESTE PARÂMETRO
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.corsConfigurationSource = corsConfigurationSource; // <-- ATRIBUA AO CAMPO
    }
    // --- FIM DA MODIFICAÇÃO DO CONSTRUTOR ---

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // --- LINHA MODIFICADA ---
                // Agora usa o campo injetado em vez de chamar o método diretamente
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource))
                // --- FIM DA MODIFICAÇÃO ---
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/v1/api/auth/**",
                                "/v1/api/vans/**",
                                "/escolas/**",
                                "/images/**",
                                "/v1/api/notifications/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- MÉTODO REMOVIDO ---
    // O @Bean CorsConfigurationSource corsConfigurationSource() foi removido daqui
    // porque agora está na classe CorsConfig.java
    // --- FIM DA REMOÇÃO ---

}