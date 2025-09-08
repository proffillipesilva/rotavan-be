package br.edu.fiec.RotaVan.features.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Responsaveis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // REMOVEMOS email e password

    // ADICIONAMOS A LIGAÇÃO PARA O USUÁRIO
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore // Evita loops infinitos na serialização
    private User user;

    @Column(nullable = false)
    private String nomeResponsavel;

    @Column(nullable = false, unique = true)
    private String cpfResponsavel;

    @Column(nullable = false)
    private String enderecoCasa;

    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Crianca> criancas;

    // REMOVEMOS toda a implementação de UserDetails
}