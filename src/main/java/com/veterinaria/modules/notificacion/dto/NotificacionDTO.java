package com.veterinaria.modules.notificacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para envío de notificaciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionDTO {

    private String destinatario; // Email del propietario
    private String asunto;
    private String mensaje;
    private String tipoNotificacion; // EMAIL, SMS
}