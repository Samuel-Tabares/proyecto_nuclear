package com.veterinaria.modules.prescripcion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para transferencia de datos de Prescripcion
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescripcionDTO {

    private Long id;

    @NotBlank(message = "El medicamento es obligatorio")
    private String medicamento;

    @NotBlank(message = "La dosis es obligatoria")
    private String dosis;

    @NotBlank(message = "La frecuencia es obligatoria")
    private String frecuencia;

    @NotNull(message = "La duración es obligatoria")
    @Positive(message = "La duración debe ser positiva")
    private Integer duracionDias;

    private String indicaciones;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @NotNull(message = "La mascota es obligatoria")
    private Long mascotaId;

    private Long historiaId; // Opcional

    // Datos adicionales para respuesta
    private String mascotaNombre;
    private String propietarioNombre;
    private String fechaCreacion;
}