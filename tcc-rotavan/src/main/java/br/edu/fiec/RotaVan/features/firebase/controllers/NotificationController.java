package br.edu.fiec.RotaVan.features.firebase.controllers; // Corrija o nome do pacote se necessário

import br.edu.fiec.RotaVan.features.firebase.dto.FcmTokenRequest;
import br.edu.fiec.RotaVan.features.firebase.dto.NotificationMessage;
import br.edu.fiec.RotaVan.features.firebase.services.NotificationService;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.services.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/notifications")
@AllArgsConstructor
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    @PutMapping("/token")
    public ResponseEntity<Void> registerFcmToken(
            Authentication authentication,
            @Valid @RequestBody FcmTokenRequest request) {

        User myUser = (User) authentication.getPrincipal();
        userService.updateFcmToken(myUser.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
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