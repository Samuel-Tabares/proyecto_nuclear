package com.veterinaria.modules.notificacion.service;

import com.veterinaria.modules.notificacion.dto.NotificacionDTO;
import com.veterinaria.modules.notificacion.factory.NotificacionFactory;
import com.veterinaria.modules.notificacion.strategy.NotificacionSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service para gestión de notificaciones
 * Usa Factory y Strategy patterns
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionService {

    private final NotificacionFactory notificacionFactory;

    @Async // Envía notificaciones de forma asíncrona
    public void enviarNotificacion(NotificacionDTO notificacion) {
        try {
            String tipo = notificacion.getTipoNotificacion() != null
                    ? notificacion.getTipoNotificacion()
                    : "EMAIL";

            NotificacionSender sender = notificacionFactory.getSender(tipo);
            sender.enviar(notificacion);

            log.info("Notificación {} enviada a {}", tipo, notificacion.getDestinatario());
        } catch (Exception e) {
            log.error("Error enviando notificación: {}", e.getMessage());
        }
    }

    // Método helper para crear notificación de cita
    public NotificacionDTO crearNotificacionCita(String email, String nombreMascota,
                                                 String fecha, String motivo) {
        return NotificacionDTO.builder()
                .destinatario(email)
                .asunto("Confirmación de Cita Veterinaria")
                .mensaje(String.format(
                        "Estimado propietario,\n\n" +
                                "Se ha agendado una cita para su mascota %s.\n" +
                                "Fecha: %s\n" +
                                "Motivo: %s\n\n" +
                                "Por favor llegue 10 minutos antes.\n\n" +
                                "Saludos,\nVetApp",
                        nombreMascota, fecha, motivo
                ))
                .tipoNotificacion("EMAIL")
                .build();
    }
}