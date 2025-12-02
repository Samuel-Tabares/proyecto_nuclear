package com.veterinaria.modules.cita.facade;

import com.veterinaria.modules.cita.dto.CitaDTO;
import com.veterinaria.modules.cita.service.CitaService;
import com.veterinaria.modules.notificacion.dto.NotificacionDTO;
import com.veterinaria.modules.notificacion.service.NotificacionService;
import com.veterinaria.shared.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Facade para simplificar operaciones complejas de Cita
 * Patrón Facade: coordina CitaService + NotificacionService
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CitaFacade {

    private final CitaService citaService;
    private final NotificacionService notificacionService;

    /**
     * Crea una cita y envía notificación al propietario automáticamente
     */
    public CitaDTO crearCitaConNotificacion(CitaDTO citaDTO) {
        // 1. Crear la cita
        CitaDTO citaCreada = citaService.crear(citaDTO);
        log.info("Cita creada: ID {}", citaCreada.getId());

        // 2. Preparar notificación
        NotificacionDTO notificacion = notificacionService.crearNotificacionCita(
                citaCreada.getPropietarioEmail(),
                citaCreada.getMascotaNombre(),
                DateUtils.formatDateTime(citaCreada.getFechaHora()),
                citaCreada.getMotivo()
        );

        // 3. Enviar notificación (asíncrono)
        notificacionService.enviarNotificacion(notificacion);
        log.info("Notificación enviada a: {}", citaCreada.getPropietarioEmail());

        return citaCreada;
    }

    /**
     * Actualiza cita y notifica cambios si hay modificación de fecha
     */
    public CitaDTO actualizarCitaConNotificacion(Long id, CitaDTO citaDTO) {
        CitaDTO citaAnterior = citaService.obtenerPorId(id);
        CitaDTO citaActualizada = citaService.actualizar(id, citaDTO);

        // Notificar solo si cambió la fecha
        if (!citaAnterior.getFechaHora().equals(citaActualizada.getFechaHora())) {
            NotificacionDTO notificacion = NotificacionDTO.builder()
                    .destinatario(citaActualizada.getPropietarioEmail())
                    .asunto("Cambio en su Cita Veterinaria")
                    .mensaje(String.format(
                            "Estimado propietario,\n\n" +
                                    "La cita de %s ha sido reprogramada.\n" +
                                    "Nueva fecha: %s\n" +
                                    "Motivo: %s\n\n" +
                                    "Saludos,\nVetApp",
                            citaActualizada.getMascotaNombre(),
                            DateUtils.formatDateTime(citaActualizada.getFechaHora()),
                            citaActualizada.getMotivo()
                    ))
                    .tipoNotificacion("EMAIL")
                    .build();

            notificacionService.enviarNotificacion(notificacion);
        }

        return citaActualizada;
    }
}