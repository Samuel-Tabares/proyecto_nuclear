package com.veterinaria.modules.prescripcion.repository;

import com.veterinaria.modules.prescripcion.entity.Prescripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository para acceso a datos de Prescripcion
 * Patrón Repository
 */
@Repository
public interface PrescripcionRepository extends JpaRepository<Prescripcion, Long> {

    List<Prescripcion> findByMascotaId(Long mascotaId);
    List<Prescripcion> findByHistoriaClinicaId(Long historiaId);
    List<Prescripcion> findByFechaFinAfter(LocalDate fecha); // Prescripciones activas
    List<Prescripcion> findByMascotaIdOrderByFechaCreacionDesc(Long mascotaId);
}