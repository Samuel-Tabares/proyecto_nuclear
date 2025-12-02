package com.veterinaria.modules.facturacion.repository;

import com.veterinaria.modules.facturacion.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para acceso a datos de Factura
 * Patrón Repository
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByNumeroFactura(String numeroFactura);
    List<Factura> findByPropietarioId(Long propietarioId);
    List<Factura> findByMascotaId(Long mascotaId);
    List<Factura> findByEstado(Factura.EstadoFactura estado);
    boolean existsByNumeroFactura(String numeroFactura);
}