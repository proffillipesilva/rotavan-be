package br.edu.fiec.RotaVan.config; // Pacote ajustado

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Captura e loga exceções gerais de forma centralizada.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        log.error("******** EXCEÇÃO GLOBAL CAPTURADA ********");
        log.error("Mensagem: {}", ex.getMessage(), ex);
        log.error("******************************************");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "Ocorreu um erro inesperado no servidor. Por favor, tente novamente.");

        // Você pode customizar a mensagem com base no tipo de exceção
        if (ex instanceof IllegalArgumentException) {
            body.put("status", HttpStatus.BAD_REQUEST.value());
            body.put("error", "Bad Request");
            body.put("message", ex.getMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        // Exceção de "Não Encontrado" (ex: RuntimeException("Usuário não encontrado"))
        // No seu service, use "throw new RuntimeException("Motorista não encontrado.")"
        if (ex instanceof RuntimeException && ex.getMessage().contains("não encontrado")) {
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Not Found");
            body.put("message", ex.getMessage());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}