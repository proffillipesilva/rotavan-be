package br.edu.fiec.RotaVan.features.user.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Responsaveis implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    String nomeResponsavel;

    @Column(nullable = false, unique = true) // Adicionar unique = true é uma boa prática para CPF
    String cpfResponsavel;

    @Column(nullable = false)
    String enderecoCasa;

    @Column(nullable = false)
    String nomeCrianca;

    @Column(nullable = false)
    String enderecoEscola;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }
}