package com.veterinaria.modules.cita.repository;

import com.veterinaria.modules.cita.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para acceso a datos de Cita
 * Patrón Repository
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByMascotaId(Long mascotaId);
    List<Cita> findByMascotaPropietarioId(Long propietarioId);
    List<Cita> findByEstado(Cita.EstadoCita estado);
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}