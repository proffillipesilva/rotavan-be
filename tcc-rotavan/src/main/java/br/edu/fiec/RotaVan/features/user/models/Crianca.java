package br.edu.fiec.RotaVan.features.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Crianca {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    @JsonIgnore
    private Responsaveis responsavel;

    @ManyToOne
    @JoinColumn(name = "escola_id")
    private Escolas escola;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "nivel_escolar", length = 100)
    private String nivelEscolar;

    @Column(length = 150)
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servico")
    private TipoServico tipoServico;

    @Column(length = 50)
    private String periodo;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;
}