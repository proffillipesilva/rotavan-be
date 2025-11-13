package br.edu.fiec.RotaVan.config;

import br.edu.fiec.RotaVan.config.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider,
            CorsConfigurationSource corsConfigurationSource
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource))
                .authorizeHttpRequests(requests -> requests
                        // Permite todas as requisições OPTIONS (Preflight de CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ----- INÍCIO DAS MUDANÇAS -----

                        // 1. Endpoints de Autenticação PÚBLICOS
                        // Apenas login e registo de responsável são permitidos publicamente.
                        .requestMatchers(
                                "/v1/api/auth/login",
                                "/v1/api/auth/register/responsavel"
                        ).permitAll()

                        // 2. Endpoints de Autenticação PROTEGIDOS
                        // Apenas um ADMIN pode registar um novo motorista ou admin.
                        .requestMatchers(
                                "/v1/api/auth/register/motorista",
                                "/v1/api/auth/register/admin"
                        ).hasRole("ADMIN") // <-- ESTA É A PROTEÇÃO!

                        // 3. Outros Endpoints Públicos
                        // Mantém as outras regras que já tinhas (Swagger, vans, etc.)
                        .requestMatchers(
                                "/v1/api/vans/**",
                                "/escolas/**",
                                "/images/**",
                                "/v1/api/notifications/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ----- FIM DAS MUDANÇAS -----

                        // 4. Regra Final: Todo o resto precisa de autenticação
                        .anyRequest().authenticated()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}