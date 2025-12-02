package com.veterinaria.modules.propietario.repository;

import com.veterinaria.modules.propietario.entity.Propietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para acceso a datos de Propietario
 * Patrón Repository
 */
@Repository
public interface PropietarioRepository extends JpaRepository<Propietario, Long> {

    Optional<Propietario> findByDocumento(String documento);
    Optional<Propietario> findByEmail(String email);
    boolean existsByDocumento(String documento);
    boolean existsByEmail(String email);
}