package br.edu.fiec.RotaVan.features.firebase.controllers; // Corrija o nome do pacote se necessário

import br.edu.fiec.RotaVan.features.firebase.dto.FcmTokenRequest;
import br.edu.fiec.RotaVan.features.firebase.dto.NotificationMessage;
import br.edu.fiec.RotaVan.features.firebase.services.NotificationService;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.services.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/notifications")
@AllArgsConstructor
@Tag(name = "Notificações (FCM)", description = "API para registro de token e envio de notificações push via Firebase")
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @PutMapping("/token")
    @Operation(summary = "Registra o token FCM do usuário logado",
            description = "Atualiza o 'fcmToken' do usuário autenticado para permitir o recebimento de notificações push.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token FCM atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Token FCM não fornecido ou inválido no corpo da requisição."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao salvar o token.")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> registerFcmToken(
            Authentication authentication,
            @Valid @RequestBody FcmTokenRequest request) {

        User myUser = (User) authentication.getPrincipal();
        userService.updateFcmToken(myUser.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    @Operation(summary = "Envia uma notificação push para um usuário (Admin/Sistema)",
            description = "Envia uma mensagem de notificação para um usuário específico (requer permissão de Admin ou uso interno do sistema).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação enviada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Usuário de destino não encontrado ou sem token FCM (RuntimeException)."),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado ou não autorizado para enviar notificações."),
            @ApiResponse(responseCode = "500", description = "Erro ao enviar notificação (FirebaseMessagingException).")
    })
    @SecurityRequirement(name = "bearerAuth") // Assumindo que é um endpoint protegido
    public ResponseEntity<String> sendNotification(@RequestBody NotificationMessage dto) {
        try {
            String response = notificationService.sendNotificationToUser(dto);
            return ResponseEntity.ok(response);
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.internalServerError().body("Erro ao enviar notificação: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}