package com.veterinaria.modules.facturacion.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para transferencia de datos de Factura
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {

    private Long id;
    private String numeroFactura;
    private String fechaEmision;

    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal total;

    private String estado;
    private String observaciones;

    @NotNull(message = "El propietario es obligatorio")
    private Long propietarioId;
    private String propietarioNombre;
    private String propietarioEmail;

    @NotNull(message = "La mascota es obligatoria")
    private Long mascotaId;
    private String mascotaNombre;

    private Long citaId;

    private List<DetalleFacturaDTO> detalles = new ArrayList<>();
}