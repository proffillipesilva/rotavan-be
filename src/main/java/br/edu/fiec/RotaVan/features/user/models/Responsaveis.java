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

    @Column(nullable = false, unique = true)
    String cpfResponsavel;

    @Column(nullable = false)
    String enderecoCasa;

    /**
     * ALTERAÇÃO 1: Relacionamento Um-para-Muitos.
     * Um responsável pode ter muitas crianças.
     * - cascade = CascadeType.ALL: Salva ou apaga as crianças junto com o responsável.
     * - orphanRemoval = true: Remove crianças da base de dados se forem removidas da lista.
     */
    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Crianca> criancas;


    // MÉTODOS DA INTERFACE USERDETAILS //

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, não estamos a usar papéis (Roles), então retornamos uma lista vazia.
        return List.of();
    }

    @Override
    public String getUsername() {
        // O Spring Security usará o email como "nome de usuário" para o login.
        return email;
    }


}