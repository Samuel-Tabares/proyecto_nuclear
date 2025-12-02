package com.veterinaria.modules.facturacion.service;

import com.veterinaria.modules.cita.entity.Cita;
import com.veterinaria.modules.cita.repository.CitaRepository;
import com.veterinaria.modules.facturacion.dto.DetalleFacturaDTO;
import com.veterinaria.modules.facturacion.dto.FacturaDTO;
import com.veterinaria.modules.facturacion.entity.DetalleFactura;
import com.veterinaria.modules.facturacion.entity.Factura;
import com.veterinaria.modules.facturacion.repository.FacturaRepository;
import com.veterinaria.modules.facturacion.strategy.CalculoFacturaStrategy;
import com.veterinaria.modules.mascota.entity.Mascota;
import com.veterinaria.modules.mascota.repository.MascotaRepository;
import com.veterinaria.modules.propietario.entity.Propietario;
import com.veterinaria.modules.propietario.repository.PropietarioRepository;
import com.veterinaria.shared.exception.ResourceNotFoundException;
import com.veterinaria.shared.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service para lógica de negocio de Factura
 * Usa Strategy pattern para cálculos
 */
@Service
@RequiredArgsConstructor
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final PropietarioRepository propietarioRepository;
    private final MascotaRepository mascotaRepository;
    private final CitaRepository citaRepository;
    private final CalculoFacturaStrategy calculoEstandar; // Inyecta estrategia por defecto

    @Transactional
    public FacturaDTO crear(FacturaDTO dto) {
        Propietario propietario = propietarioRepository.findById(dto.getPropietarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado"));

        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada"));

        Factura factura = new Factura();
        factura.setNumeroFactura(generarNumeroFactura());
        factura.setPropietario(propietario);
        factura.setMascota(mascota);
        factura.setObservaciones(dto.getObservaciones());

        // Asociar cita si existe
        if (dto.getCitaId() != null) {
            Cita cita = citaRepository.findById(dto.getCitaId()).orElse(null);
            factura.setCita(cita);
        }

        // Agregar detalles y calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetalleFacturaDTO detalleDTO : dto.getDetalles()) {
            DetalleFactura detalle = new DetalleFactura();
            detalle.setDescripcion(detalleDTO.getDescripcion());
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
            detalle.setSubtotal(detalleDTO.getPrecioUnitario().multiply(BigDecimal.valueOf(detalleDTO.getCantidad())));
            detalle.setFactura(factura);

            factura.getDetalles().add(detalle);
            subtotal = subtotal.add(detalle.getSubtotal());
        }

        // Calcular total usando estrategia
        factura.setSubtotal(subtotal);
        factura.setImpuesto(new BigDecimal("19.00")); // 19% IVA
        factura.setTotal(calculoEstandar.calcularTotal(subtotal, null));

        Factura saved = facturaRepository.save(factura);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<FacturaDTO> listarTodas() {
        return facturaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FacturaDTO obtenerPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada: " + id));
        return toDTO(factura);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Factura no encontrada: " + id);
        }
        facturaRepository.deleteById(id);
    }

    // Generar número único de factura
    private String generarNumeroFactura() {
        String numero;
        do {
            numero = "F-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (facturaRepository.existsByNumeroFactura(numero));
        return numero;
    }

    // Conversión Entity -> DTO
    private FacturaDTO toDTO(Factura entity) {
        FacturaDTO dto = new FacturaDTO();
        dto.setId(entity.getId());
        dto.setNumeroFactura(entity.getNumeroFactura());
        dto.setFechaEmision(DateUtils.formatDateTime(entity.getFechaEmision()));
        dto.setSubtotal(entity.getSubtotal());
        dto.setImpuesto(entity.getImpuesto());
        dto.setTotal(entity.getTotal());
        dto.setEstado(entity.getEstado().name());
        dto.setObservaciones(entity.getObservaciones());
        dto.setPropietarioId(entity.getPropietario().getId());
        dto.setPropietarioNombre(entity.getPropietario().getNombre() + " " + entity.getPropietario().getApellido());
        dto.setPropietarioEmail(entity.getPropietario().getEmail());
        dto.setMascotaId(entity.getMascota().getId());
        dto.setMascotaNombre(entity.getMascota().getNombre());

        if (entity.getCita() != null) {
            dto.setCitaId(entity.getCita().getId());
        }

        // Convertir detalles
        List<DetalleFacturaDTO> detallesDTO = entity.getDetalles().stream()
                .map(d -> {
                    DetalleFacturaDTO dDto = new DetalleFacturaDTO();
                    dDto.setId(d.getId());
                    dDto.setDescripcion(d.getDescripcion());
                    dDto.setCantidad(d.getCantidad());
                    dDto.setPrecioUnitario(d.getPrecioUnitario());
                    dDto.setSubtotal(d.getSubtotal());
                    return dDto;
                })
                .collect(Collectors.toList());
        dto.setDetalles(detallesDTO);

        return dto;
    }
}