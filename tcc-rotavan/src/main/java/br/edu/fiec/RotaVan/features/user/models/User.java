package br.edu.fiec.RotaVan.features.user.models;

import io.swagger.v3.oas.annotations.media.Schema; // <-- IMPORTAÇÃO ADICIONADA
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
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String nome; // CAMPO ADICIONADO previamente

    @Enumerated(EnumType.STRING) // Guarda o nome do enum (ex: "ROLE_RESPONSAVEL") em vez de um número
    @Column(nullable = false)
    private Role role;

    @Column
    private String picture;

    // --- CAMPO FCM ADICIONADO ---
    @Column
    private String fcmToken;
    // --- FIM DO CAMPO FCM ---

    // --- INÍCIO DA CORREÇÃO ---
    @Column
    @Schema(description = "Endereço principal do usuário (usado para Motorista).",
            example = "Rua do Motorista, 500, Indaiatuba-SP")
    private String endereco;
    // --- FIM DA CORREÇÃO ---


    // Relacionamento Um-para-Um com o perfil do Responsável
    // cascade = CascadeType.ALL: Se o User for apagado, o perfil também é.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Responsaveis responsavelProfile;

    // Relacionamento Um-para-Um com o perfil do Motorista
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
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
    public enum Role {
        ROLE_RESPONSAVEL,
        ROLE_MOTORISTA,
        ROLE_ADMIN
    }
}