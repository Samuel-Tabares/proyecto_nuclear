package com.veterinaria.modules.mascota.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para transferencia de datos de Mascota
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MascotaDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "Nombre entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    private String especie;

    private String raza;

    private LocalDate fechaNacimiento;

    private String sexo;

    private String color;

    @Positive(message = "El peso debe ser positivo")
    private Double peso;

    @NotNull(message = "El propietario es obligatorio")
    private Long propietarioId;

    // Datos del propietario para respuesta
    private String propietarioNombre;
    private String propietarioEmail;

    private String fechaRegistro;
}