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
 * Usa JavaMailSender de Spring
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificacionSender implements NotificacionSender {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Override
    public void enviar(NotificacionDTO notificacion) {
        if (!mailEnabled) {
            log.warn("Email deshabilitado - No se envió: {}", notificacion.getAsunto());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(notificacion.getDestinatario());
            message.setSubject(notificacion.getAsunto());
            message.setText(notificacion.getMensaje());

            mailSender.send(message);
            log.info("Email enviado a: {}", notificacion.getDestinatario());
        } catch (Exception e) {
            log.error("Error al enviar email: {}", e.getMessage());
            throw new RuntimeException("Error enviando email: " + e.getMessage());
        }
    }

    @Override
    public String getTipo() {
        return "EMAIL";
    }
}