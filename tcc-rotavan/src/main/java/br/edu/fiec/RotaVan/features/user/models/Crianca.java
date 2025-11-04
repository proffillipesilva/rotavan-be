package br.edu.fiec.RotaVan.features.user.models;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate; // Importar LocalDate
import java.util.UUID;

@Entity
// Não precisa @Table(name="criancas") se o nome da classe for Crianca,
// mas pode adicionar se preferir explicitar.
@Data
public class Crianca {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id") // Manter nome da FK do SQL se preferir
    @JsonIgnore
    private Responsaveis responsavel;


    @ManyToOne // FetchType.EAGER é o padrão para ManyToOne
    @JoinColumn(name = "escola_id") // Manter nome da FK do SQL se preferir
    private Escolas escola;

    // --- CAMPOS ADICIONADOS ---
    @Column(name = "data_nascimento") // Mapeia para a coluna do SQL
    private LocalDate dataNascimento;

    @Column(name = "nivel_escolar", length = 50)
    private String nivelEscolar;

    @Column(length = 150)
    private String endereco;

    @Column(length = 20)
    private String telefone;
    // --- FIM DOS CAMPOS ADICIONADOS ---
}