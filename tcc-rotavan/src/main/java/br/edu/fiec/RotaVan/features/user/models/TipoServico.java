package br.edu.fiec.RotaVan.features.user.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Define o tipo de serviço de transporte contratado (Ex: Só ida, só volta, ou ambos)")
public enum TipoServico {
    IDA,
    VOLTA,
    IDA_E_VOLTA
}