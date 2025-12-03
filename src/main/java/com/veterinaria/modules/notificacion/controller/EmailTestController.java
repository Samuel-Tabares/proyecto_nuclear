package com.veterinaria.modules.notificacion.controller;

import com.veterinaria.modules.notificacion.dto.NotificacionDTO;
import com.veterinaria.modules.notificacion.service.NotificacionService;
import com.veterinaria.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para probar el envío de emails
 * Útil para debugging en producción
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class EmailTestController {

    private final JavaMailSender mailSender;
    private final NotificacionService notificacionService;

    @Value("${spring.mail.username:NO_CONFIGURADO}")
    private String mailUsername;

    @Value("${spring.mail.host:NO_CONFIGURADO}")
    private String mailHost;

    @Value("${spring.mail.port:0}")
    private int mailPort;

    @Value("${app.mail.enabled:false}")
    private boolean mailEnabled;

    @Value("${app.mail.from:NO_CONFIGURADO}")
    private String mailFrom;

    /**
     * Verifica la configuración de email sin enviar nada
     */
    @GetMapping("/email-config")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEmailConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("host", mailHost);
        config.put("port", mailPort);
        config.put("username", mailUsername);
        config.put("from", mailFrom);
        config.put("enabled", mailEnabled);
        config.put("passwordConfigured", mailUsername != null && !mailUsername.equals("NO_CONFIGURADO"));

        log.info("Email config solicitada: {}", config);

        return ResponseEntity.ok(ApiResponse.success(config, "Configuración de email"));
    }

    /**
     * Envía un email de prueba
     * USO: POST /api/test/send-email?to=samitabaleon@email.com
     */
    @PostMapping("/send-email")
    public ResponseEntity<ApiResponse<String>> sendTestEmail(@RequestParam String to) {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("           PRUEBA DE ENVÍO DE EMAIL                        ");
        log.info("═══════════════════════════════════════════════════════════");
        log.info("Destinatario: {}", to);
        log.info("From: {}", mailFrom);
        log.info("Host: {}", mailHost);
        log.info("Port: {}", mailPort);
        log.info("Enabled: {}", mailEnabled);

        if (!mailEnabled) {
            return ResponseEntity.ok(ApiResponse.error("Email está deshabilitado en configuración"));
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(to);
            message.setSubject("🧪 Test Email - VetApp");
            message.setText("Este es un email de prueba desde VetApp.\n\n" +
                    "Si recibes este mensaje, la configuración de email está funcionando correctamente.\n\n" +
                    "Configuración usada:\n" +
                    "- Host: " + mailHost + "\n" +
                    "- Port: " + mailPort + "\n" +
                    "- From: " + mailFrom + "\n\n" +
                    "Saludos,\nVetApp");

            log.info("Enviando email de prueba...");
            mailSender.send(message);
            log.info("✅ Email de prueba enviado exitosamente!");

            return ResponseEntity.ok(ApiResponse.success(
                    "Email enviado a " + to,
                    "Email de prueba enviado exitosamente"
            ));

        } catch (Exception e) {
            log.error("❌ Error enviando email de prueba: {}", e.getMessage());
            log.error("Stack trace:", e);

            return ResponseEntity.ok(ApiResponse.error(
                    "Error enviando email: " + e.getMessage() +
                            " | Tipo: " + e.getClass().getSimpleName()
            ));
        }
    }

    /**
     * Prueba el servicio de notificación completo
     */
    @PostMapping("/send-notification")
    public ResponseEntity<ApiResponse<String>> sendTestNotification(@RequestParam String to) {
        log.info("Probando servicio de notificación completo...");

        try {
            NotificacionDTO notificacion = NotificacionDTO.builder()
                    .destinatario(to)
                    .asunto("🧪 Test Notificación - VetApp")
                    .mensaje("Este es un test del sistema de notificaciones.\n\n" +
                            "Si recibes este mensaje, el servicio está funcionando.\n\n" +
                            "Saludos,\nVetApp")
                    .tipoNotificacion("EMAIL")
                    .build();

            notificacionService.enviarNotificacion(notificacion);

            return ResponseEntity.ok(ApiResponse.success(
                    "Notificación procesada para " + to,
                    "Revisa los logs para ver el resultado"
            ));

        } catch (Exception e) {
            log.error("Error en test de notificación: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Error: " + e.getMessage()));
        }
    }
}