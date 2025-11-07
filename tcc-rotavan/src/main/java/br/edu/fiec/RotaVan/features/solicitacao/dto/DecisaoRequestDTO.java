package br.edu.fiec.RotaVan.features.solicitacao.dto;

import lombok.Data;

// DTO para o PASSO 3 (Decisão do Motorista)
@Data
public class DecisaoRequestDTO {
    private String status; // "ACEITO" ou "RECUSADO"
}