package br.edu.fiec.RotaVan.features.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(exclude = {"responsavelProfile", "motoristaProfile"}) // evita loop no toString()
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private String picture;

    @Column
    private String fcmToken;

    @Column
    @Schema(
            description = "Endereço principal do usuário (usado para Motorista).",
            example = "Rua do Motorista, 500, Indaiatuba-SP"
    )
    private String endereco;

    // -------- RELAÇÃO COM RESPONSÁVEL --------
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore // evita loop na hora de serializar /me
    private Responsaveis responsavelProfile;

    // -------- RELAÇÃO COM MOTORISTA --------
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Motoristas motoristaProfile;

    // ===============================
    // MÉTODOS DA INTERFACE UserDetails
    // ===============================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // Enum de papéis
    public enum Role {
        ROLE_RESPONSAVEL,
        ROLE_MOTORISTA,
        ROLE_ADMIN,
        ROLE_ESCOLA
    }
}