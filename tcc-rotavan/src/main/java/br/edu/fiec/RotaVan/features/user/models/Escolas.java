package br.edu.fiec.RotaVan.features.user.models;

import io.swagger.v3.oas.annotations.media.Schema; // 1. IMPORTAR O SCHEMA
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
@Schema(description = "Representa os dados de uma Escola")
public class Escolas {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    @Schema(description = "ID único da escola (UUID).",
            example = "e1a2b3c4-d5e6-f789-0123-456789abcdef")
    private UUID id;

    @Column(nullable = false)
    @Schema(description = "Nome oficial da escola.",
            example = "Colégio Exemplo de Indaiatuba",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Column(nullable = false, unique = true)
    @Schema(description = "CNPJ da escola (somente números).",
            example = "12345678000199",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String cnpj;

    @Column(nullable = false)
    @Schema(description = "Endereço completo da escola.",
            example = "Avenida Principal, 1000, Centro, Indaiatuba-SP",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String endereco;

    @Schema(description = "Telefone principal de contato da escola.",
            example = "1938887766")
    private String telefone;

}