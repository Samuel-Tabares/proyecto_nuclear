package com.veterinaria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuración para habilitar procesamiento asíncrono
 * Permite enviar notificaciones sin bloquear requests
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}