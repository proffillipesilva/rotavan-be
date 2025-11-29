package br.edu.fiec.RotaVan.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API RotaVan",
                version = "1.0",
                description = "Documentação da API de Transporte Escolar RotaVan"
        ),
        // Isto aplica a segurança a TODOS os endpoints automaticamente
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Insira o token JWT aqui para aceder aos endpoints protegidos."
)
public class OpenApiConfig {
    // Esta classe serve apenas para configurações via anotações
}