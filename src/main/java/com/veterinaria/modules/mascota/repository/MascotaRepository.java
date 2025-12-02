package com.veterinaria.modules.mascota.repository;

import com.veterinaria.modules.mascota.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para acceso a datos de Mascota
 * Patrón Repository
 */
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    List<Mascota> findByPropietarioId(Long propietarioId);
    List<Mascota> findByEspecieIgnoreCase(String especie);
}