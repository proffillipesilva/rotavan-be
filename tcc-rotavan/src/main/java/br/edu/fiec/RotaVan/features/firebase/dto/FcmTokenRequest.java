package br.edu.fiec.RotaVan.features.firebase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO (Data Transfer Object) para enviar/atualizar o token do Firebase Cloud Messaging (FCM).")
public class FcmTokenRequest {

    @NotBlank(message = "O token FCM não pode ser vazio.")
    @Schema(description = "Token de registro do dispositivo gerado pelo Firebase.",
            example = "cE...aA:APA91b..._e4",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String fcmToken;
}