package com.veterinaria.modules.cita.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Cita
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {

    private Long id;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    private String observaciones;

    private String estado;

    @NotNull(message = "La mascota es obligatoria")
    private Long mascotaId;

    // Datos adicionales para respuesta
    private String mascotaNombre;
    private String propietarioNombre;
    private String propietarioEmail;
    private String fechaCreacion;
}