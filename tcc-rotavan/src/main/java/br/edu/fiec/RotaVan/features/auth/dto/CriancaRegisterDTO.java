package br.edu.fiec.RotaVan.features.auth.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CriancaRegisterDTO {
    private String nome;
    private UUID escolaId;
}