package br.edu.fiec.RotaVan.features.firebase.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FcmTokenRequest {
    @NotBlank(message = "O token FCM não pode ser vazio.")
    private String fcmToken;
}