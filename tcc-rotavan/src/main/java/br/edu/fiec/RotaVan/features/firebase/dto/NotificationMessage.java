package br.edu.fiec.RotaVan.features.firebase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO (Data Transfer Object) para enviar uma notificação push via Firebase.")
public class NotificationMessage {

    @Schema(description = "ID (UUID) do usuário que receberá a notificação.",
            example = "u1a2b3c4-d5e6-f789-0123-456789user",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId; // ID do usuário que receberá a notificação

    @Schema(description = "Título da notificação (o que aparece em negrito).",
            example = "Atualização da Rota!",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Corpo da mensagem da notificação.",
            example = "O motorista Carlos está a 5 minutos do seu local.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}