package com.veterinaria.modules.notificacion.factory;

import com.veterinaria.modules.notificacion.strategy.NotificacionSender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory para crear instancias de NotificacionSender
 * Patrón Factory + Strategy
 */
@Component
public class NotificacionFactory {

    private final Map<String, NotificacionSender> senders;

    // Inyecta automáticamente todas las implementaciones de NotificacionSender
    public NotificacionFactory(List<NotificacionSender> senderList) {
        this.senders = senderList.stream()
                .collect(Collectors.toMap(
                        NotificacionSender::getTipo,
                        Function.identity()
                ));
    }

    public NotificacionSender getSender(String tipo) {
        NotificacionSender sender = senders.get(tipo.toUpperCase());
        if (sender == null) {
            throw new IllegalArgumentException("Tipo de notificación no soportado: " + tipo);
        }
        return sender;
    }
}