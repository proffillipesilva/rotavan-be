package br.edu.fiec.RotaVan.features.user.models;

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
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String nome; // CAMPO ADICIONADO

    @Enumerated(EnumType.STRING) // Guarda o nome do enum (ex: "ROLE_RESPONSAVEL") em vez de um número
    @Column(nullable = false)
    private Role role;

    @Column
    private String picture;

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

    public enum Role {
        ROLE_RESPONSAVEL,
        ROLE_MOTORISTA,
        ROLE_ADMIN
    }
}