package br.edu.fiec.RotaVan.features.firebase.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage {
    private String userId; // ID do usuário que receberá a notificação
    private String title;
    private String message;
}