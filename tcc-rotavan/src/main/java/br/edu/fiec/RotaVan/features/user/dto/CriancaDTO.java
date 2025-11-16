package br.edu.fiec.RotaVan.features.user.dto;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.TipoServico; // <-- 1. IMPORTAR
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal; // <-- 2. IMPORTAR
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriancaDTO {

    private UUID id;
    private String nome;
    private Escolas escola;
    private LocalDate dataNascimento;
    private String nivelEscolar;
    private String endereco;

    private TipoServico tipoServico; // Ida; Volta; Ida e Volta
    private String periodo; // Manhã, Tarde ou Noite
    private BigDecimal latitude;
    private BigDecimal longitude;

}