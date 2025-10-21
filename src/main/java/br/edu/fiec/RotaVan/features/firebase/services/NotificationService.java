package br.edu.fiec.RotaVan.features.firebase.services;

import br.edu.fiec.RotaVan.features.firebase.dto.NotificationMessage;
import br.edu.fiec.RotaVan.features.user.models.User;
import br.edu.fiec.RotaVan.features.user.repositories.UserRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final UserRepository userRepository;
    private final FirebaseMessaging firebaseMessaging;

    public NotificationService(UserRepository userRepository, FirebaseApp firebaseApp) {
        this.userRepository = userRepository;
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
    }

    public String sendNotificationToUser(NotificationMessage dto) throws FirebaseMessagingException {
        log.info("Tentando enviar notificação para userId: {}", dto.getUserId());

        Optional<User> userOptional = userRepository.findById(UUID.fromString(dto.getUserId()));

        if (userOptional.isEmpty()) {
            log.error("Usuário com ID {} não encontrado.", dto.getUserId());
            throw new RuntimeException("Usuário com ID " + dto.getUserId() + " não encontrado.");
        }

        User user = userOptional.get();
        String fcmToken = user.getFcmToken();

        if (fcmToken == null || fcmToken.trim().isEmpty()) {
            log.error("Token FCM não encontrado para o usuário ID: {}", dto.getUserId());
            throw new RuntimeException("Token FCM não encontrado para o usuário ID: " + dto.getUserId());
        }

        log.info("Encontrado token FCM: {} para utilizador {}", fcmToken, dto.getUserId());

        Notification notification = Notification.builder()
                .setTitle(dto.getTitle())
                .setBody(dto.getMessage())
                .build();

        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build();

        try {
            log.info("Enviando mensagem para o Firebase...");
            String response = firebaseMessaging.send(message);
            log.info("Firebase respondeu com sucesso. ID da mensagem: {}", response);
            return response;
        } catch (FirebaseMessagingException e) {
            log.error("Erro ao enviar mensagem via Firebase para o token {}: {}", fcmToken, e.getMessage(), e);
            throw e;
        }
    }
}