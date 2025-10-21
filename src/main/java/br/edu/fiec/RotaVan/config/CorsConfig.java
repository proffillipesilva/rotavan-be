package br.edu.fiec.RotaVan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Abordagem 1: Usando WebMvcConfigurer (como no projeto de referência estoque-back)
    // Escolha esta OU a Abordagem 2 (Bean CorsConfigurationSource), não ambas.
    /*
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos os caminhos
                .allowedOrigins("*") // Permite todas as origens (ajuste em produção!)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // Métodos permitidos
                .allowedHeaders("*") // Permite todos os cabeçalhos
                //.allowCredentials(true) // Descomente se precisar enviar cookies/credenciais
                .maxAge(3600); // Cache da resposta preflight (OPTIONS) por 1 hora
    }
    */

    // Abordagem 2: Definindo um Bean CorsConfigurationSource (como você já tem na SecurityConfig)
    // Escolha esta OU a Abordagem 1, não ambas.
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Permite todas as origens
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")); // Métodos permitidos
        configuration.setAllowedHeaders(Arrays.asList("*")); // Permite todos os cabeçalhos
        //configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todos os caminhos
        return source;
    }
}