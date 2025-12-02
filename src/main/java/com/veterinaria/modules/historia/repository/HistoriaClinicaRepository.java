package com.veterinaria.modules.historia.repository;

import com.veterinaria.modules.historia.entity.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para acceso a datos de Historia Clínica
 * Patrón Repository
 */
@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    List<HistoriaClinica> findByMascotaId(Long mascotaId);
    List<HistoriaClinica> findByMascotaIdOrderByFechaConsultaDesc(Long mascotaId);
    List<HistoriaClinica> findByFechaConsultaBetween(LocalDateTime inicio, LocalDateTime fin);
}