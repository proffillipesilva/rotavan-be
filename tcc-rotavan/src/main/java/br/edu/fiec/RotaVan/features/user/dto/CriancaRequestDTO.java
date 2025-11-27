package br.edu.fiec.RotaVan.features.user.dto;

import br.edu.fiec.RotaVan.features.user.models.Escolas;
import br.edu.fiec.RotaVan.features.user.models.TipoServico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriancaRequestDTO {

    private String nome;
    private String escolaId;
    private LocalDate dataNascimento;
    private String nivelEscolar;
    private String endereco;


}