package com.veterinaria.modules.notificacion.strategy;

import com.veterinaria.modules.notificacion.dto.NotificacionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Implementación Strategy para envío por Email
 * Usa JavaMailSender de Spring con mejor manejo de errores
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificacionSender implements NotificacionSender {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:stabares_26@cue.edu.co}")
    private String fromEmail;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Override
    public void enviar(NotificacionDTO notificacion) {
        log.info("=== INICIO ENVÍO EMAIL ===");
        log.info("Mail enabled: {}", mailEnabled);
        log.info("From email: {}", fromEmail);
        log.info("Destinatario: {}", notificacion.getDestinatario());
        log.info("Asunto: {}", notificacion.getAsunto());

        if (!mailEnabled) {
            log.warn("⚠️ Email deshabilitado en configuración - No se envió: {}", notificacion.getAsunto());
            return;
        }

        if (notificacion.getDestinatario() == null || notificacion.getDestinatario().isEmpty()) {
            log.error("❌ Destinatario vacío o nulo");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(notificacion.getDestinatario());
            message.setSubject(notificacion.getAsunto());
            message.setText(notificacion.getMensaje());

            log.info("📧 Intentando enviar email...");
            mailSender.send(message);
            log.info("✅ Email enviado exitosamente a: {}", notificacion.getDestinatario());

        } catch (Exception e) {
            log.error("❌ Error al enviar email: {}", e.getMessage());
            log.error("❌ Tipo de error: {}", e.getClass().getName());
            log.error("❌ Stack trace:", e);
            // NO relanzamos la excepción para que no rompa el flujo principal
            // pero dejamos el log para debugging
        }

        log.info("=== FIN ENVÍO EMAIL ===");
    }

    @Override
    public String getTipo() {
        return "EMAIL";
    }
}