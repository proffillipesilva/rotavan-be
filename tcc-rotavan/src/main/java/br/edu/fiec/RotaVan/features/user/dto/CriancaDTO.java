package br.edu.fiec.RotaVan.features.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriancaDTO {
    int id;
    String nome;
    int idade;
    String nivel;
    String escola;
}
