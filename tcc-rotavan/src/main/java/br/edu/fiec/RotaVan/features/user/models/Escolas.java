package br.edu.fiec.RotaVan.features.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- 1. ADICIONAR IMPORT
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
// import jakarta.validation.constraints.Email; // <-- REMOVER IMPORT (se existir)
import lombok.Data;

import java.math.BigDecimal;
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

    // --- 2. LIGAÇÃO COM USER (ADICIONADO) ---
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    @Schema(description = "Referência interna ao usuário (User) associado (ignorado no JSON).")
    private User user;
    // --- FIM DA ADIÇÃO ---

    // --- 3. CAMPO REMOVIDO ---
    // @Email
    // @Column(nullable = false, unique = true)
    // private String email; // <-- REMOVIDO (O email agora fica na entidade User)
    // --- FIM DA REMOÇÃO ---

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

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

}