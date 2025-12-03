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

    /**
     * Envía notificación de forma SÍNCRONA para mejor debugging en producción
     * Cambiar a @Async cuando se confirme que funciona
     */
    public void enviarNotificacion(NotificacionDTO notificacion) {
        log.info("=== NotificacionService.enviarNotificacion INICIADO ===");
        log.info("Destinatario: {}", notificacion.getDestinatario());
        log.info("Asunto: {}", notificacion.getAsunto());
        log.info("Tipo: {}", notificacion.getTipoNotificacion());

        try {
            String tipo = notificacion.getTipoNotificacion() != null
                    ? notificacion.getTipoNotificacion()
                    : "EMAIL";

            log.info("Obteniendo sender para tipo: {}", tipo);
            NotificacionSender sender = notificacionFactory.getSender(tipo);

            log.info("Sender obtenido: {}", sender.getClass().getName());
            sender.enviar(notificacion);

            log.info("✅ Notificación {} procesada para: {}", tipo, notificacion.getDestinatario());
        } catch (Exception e) {
            log.error("❌ Error en NotificacionService: {}", e.getMessage());
            log.error("❌ Stack trace completo:", e);
            // No relanzamos para no romper el flujo de citas/facturas
        }

        log.info("=== NotificacionService.enviarNotificacion FINALIZADO ===");
    }

    /**
     * Versión asíncrona - usar cuando se confirme que el email funciona
     */
    @Async
    public void enviarNotificacionAsync(NotificacionDTO notificacion) {
        enviarNotificacion(notificacion);
    }

    // Método helper para crear notificación de cita
    public NotificacionDTO crearNotificacionCita(String email, String nombreMascota,
                                                 String fecha, String motivo) {
        log.info("Creando notificación de cita para: {} - Mascota: {}", email, nombreMascota);

        return NotificacionDTO.builder()
                .destinatario(email)
                .asunto("Confirmación de Cita Veterinaria - VetApp")
                .mensaje(String.format(
                        "═══════════════════════════════════════════════════════════%n" +
                                "           CONFIRMACIÓN DE CITA VETERINARIA                %n" +
                                "═══════════════════════════════════════════════════════════%n%n" +
                                "Estimado propietario,%n%n" +
                                "Se ha agendado una cita para su mascota %s.%n%n" +
                                "📅 Fecha: %s%n" +
                                "📝 Motivo: %s%n%n" +
                                "Por favor llegue 10 minutos antes de su cita.%n%n" +
                                "═══════════════════════════════════════════════════════════%n" +
                                "Saludos cordiales,%n" +
                                "VetApp - Sistema de Gestión Veterinaria%n" +
                                "═══════════════════════════════════════════════════════════%n",
                        nombreMascota, fecha, motivo
                ))
                .tipoNotificacion("EMAIL")
                .build();
    }
}