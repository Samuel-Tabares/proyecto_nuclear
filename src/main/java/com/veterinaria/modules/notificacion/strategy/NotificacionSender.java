package com.veterinaria.modules.notificacion.strategy;

import com.veterinaria.modules.notificacion.dto.NotificacionDTO;

/**
 * Interfaz Strategy para envío de notificaciones
 * Permite diferentes implementaciones (Email, SMS, Push)
 */
public interface NotificacionSender {
    void enviar(NotificacionDTO notificacion);
    String getTipo();
}