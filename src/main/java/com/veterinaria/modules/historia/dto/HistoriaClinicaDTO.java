package com.veterinaria.modules.historia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para transferencia de datos de Historia Clínica
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinicaDTO {

    private Long id;

    @NotNull(message = "La fecha de consulta es obligatoria")
    private LocalDateTime fechaConsulta;

    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnostico;

    private String sintomas;

    private String tratamiento;

    private String observaciones;

    @Positive(message = "El peso debe ser positivo")
    private Double pesoRegistrado;

    @Positive(message = "La temperatura debe ser positiva")
    private Double temperatura;

    @NotNull(message = "La mascota es obligatoria")
    private Long mascotaId;

    // Datos adicionales para respuesta
    private String mascotaNombre;
    private String propietarioNombre;
    private String fechaCreacion;
    private String fechaModificacion;
}