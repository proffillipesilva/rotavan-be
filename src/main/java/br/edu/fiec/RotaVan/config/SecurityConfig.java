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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Aplica configuração CORS
                .authorizeHttpRequests(requests -> requests
                        // --- LINHA MODIFICADA ---
                        .requestMatchers(
                                "/v1/api/auth/**",       // Autenticação
                                "/v1/api/vans/**",        // Vans (se aplicável)
                                "/escolas/**",           // Escolas
                                "/images/**",            // Imagens
                                "/v1/api/notifications/**" // Notificações adicionadas aqui
                        ).permitAll() // Permite acesso público
                        // --- FIM DA MODIFICAÇÃO ---
                        .anyRequest().authenticated() // Qualquer outra requisição exige autenticação
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider) // Adiciona o provedor de autenticação
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT

        return http.build();
    }



    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Permite todas as origens (ajuste se necessário)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")); // Permite métodos comuns
        configuration.setAllowedHeaders(Arrays.asList("*")); // Permite todos os cabeçalhos
        //configuration.setAllowCredentials(true); // Se precisar de cookies/sessões
        configuration.setMaxAge(3600L); // Cache preflight
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todos os caminhos
        return source;
    }

}