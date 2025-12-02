package com.veterinaria.modules.facturacion.strategy;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interfaz Strategy para diferentes tipos de cálculo de factura
 * Permite implementar distintas estrategias de facturación
 */
public interface CalculoFacturaStrategy {
    BigDecimal calcularTotal(BigDecimal subtotal, List<Object> parametros);
    String getTipo();
}