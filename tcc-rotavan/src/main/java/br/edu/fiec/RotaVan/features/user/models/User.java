package br.edu.fiec.RotaVan.features.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users") // Usar um nome de tabela diferente de "user" que pode ser uma palavra reservada
@Data // Garante que getters, setters, etc., são gerados pelo Lombok
@Schema(description = "Representa a entidade principal de usuário, usada para autenticação e dados comuns.")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do usuário (UUID).",
            example = "u1a2b3c4-d5e6-f789-0123-456789user")
    private UUID id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Email de login do usuário (deve ser único).",
            example = "usuario@email.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Column(nullable = false)
    @Schema(description = "Senha criptografada (hashed) do usuário.",
            example = "$2a$10$abcdef... (hash)",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Column
    @Schema(description = "Nome completo do usuário.",
            example = "Nome Sobrenome")
    private String nome; // CAMPO ADICIONADO previamente

    @Enumerated(EnumType.STRING) // Guarda o nome do enum (ex: "ROLE_RESPONSAVEL") em vez de um número
    @Column(nullable = false)
    @Schema(description = "Define o nível de permissão do usuário.",
            example = "ROLE_RESPONSAVEL",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Role role;

    @Column
    @Schema(description = "Chave S3 (ou URL) da foto de perfil.",
            example = "profiles/foto.jpg")
    private String picture;

    // --- CAMPO FCM ADICIONADO ---
    @Column
    @Schema(description = "Token do Firebase Cloud Messaging (FCM) para notificações push.",
            example = "c...:A...a")
    private String fcmToken;
    // --- FIM DO CAMPO FCM ---

    // Relacionamento Um-para-Um com o perfil do Responsável
    // cascade = CascadeType.ALL: Se o User for apagado, o perfil também é.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Schema(description = "Perfil de responsável associado (preenchido se 'role' for ROLE_RESPONSAVEL).")
    private Responsaveis responsavelProfile;

    // Relacionamento Um-para-Um com o perfil do Motorista
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Schema(description = "Perfil de motorista associado (preenchido se 'role' for ROLE_MOTORISTA).")
    private Motoristas motoristaProfile;


    // Métodos da Interface UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        // O email é usado como username para autenticação
        return email;
    }

    // Estes métodos podem retornar true por padrão se não precisar de lógica extra
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // Enumeração para os papéis (Roles) dos utilizadores
    @Schema(description = "Define os níveis de permissão (papéis) no sistema.")
    public enum Role {
        ROLE_RESPONSAVEL,
        ROLE_MOTORISTA,
        ROLE_ADMIN
    }
}