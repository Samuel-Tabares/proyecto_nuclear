package com.veterinaria.modules.prescripcion.entity;

import com.veterinaria.modules.historia.entity.HistoriaClinica;
import com.veterinaria.modules.mascota.entity.Mascota;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Prescripcion - Medicamentos recetados a mascotas
 */
@Entity
@Table(name = "prescripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String medicamento;

    @Column(nullable = false, length = 500)
    private String dosis;

    @Column(nullable = false, length = 500)
    private String frecuencia;

    @Column(nullable = false)
    private Integer duracionDias;

    @Column(length = 1000)
    private String indicaciones;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Relación con Mascota
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    // Relación opcional con Historia Clínica
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_id")
    private HistoriaClinica historiaClinica;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        // Calcular fecha fin automáticamente
        if (fechaFin == null && fechaInicio != null && duracionDias != null) {
            fechaFin = fechaInicio.plusDays(duracionDias);
        }
    }
}