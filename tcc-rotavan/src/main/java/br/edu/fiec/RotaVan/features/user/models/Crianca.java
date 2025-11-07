package br.edu.fiec.RotaVan.features.user.models;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.Responsaveis;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Schema(description = "Representa os dados de um Dependente (aluno)")
public class Crianca {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID único do dependente (UUID).",
            example = "c1b2a3d4-e5f6-7890-1234-567890abcdef")
    private UUID id;

    @Column(nullable = false)
    @Schema(description = "Nome completo do dependente.",
            example = "Enzo Gabriel da Silva",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    @JsonIgnore
    @Schema(description = "Referência interna ao responsável (ignorado no JSON).")
    private Responsaveis responsavel;

    @ManyToOne
    @JoinColumn(name = "escola_id")
    @Schema(description = "Objeto da escola onde o dependente estuda.")
    private Escolas escola;

    // --- CAMPOS ADICIONADOS ---
    @Column(name = "data_nascimento")
    @Schema(description = "Data de nascimento do dependente.",
            example = "2015-08-20")
    private LocalDate dataNascimento;

    @Column(name = "nivel_escolar", length = 50)
    @Schema(description = "Nível de ensino do dependente.",
            example = "Ensino Fundamental I")
    private String nivelEscolar;

    @Column(length = 150)
    @Schema(description = "Endereço residencial do dependente (se for diferente do responsável).",
            example = "Rua das Palmeiras, 456, Apto 12")
    private String endereco;

    @Column(length = 20)
    @Schema(description = "Telefone de contato do dependente (ou secundário).",
            example = "19999887766")
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servico")
    @Schema(description = "Define o tipo de serviço de transporte contratado.",
            example = "IDA_E_VOLTA")
    private TipoServico tipoServico;
    // --- FIM DOS CAMPOS ADICIONADOS ---
}