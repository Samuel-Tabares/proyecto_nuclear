package com.veterinaria.modules.facturacion.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Strategy para cálculo con descuento personalizado + IVA
 */
@Component
public class CalculoConDescuentoStrategy implements CalculoFacturaStrategy {

    private static final BigDecimal IVA = new BigDecimal("0.19");

    @Override
    public BigDecimal calcularTotal(BigDecimal subtotal, List<Object> parametros) {
        // Primer parámetro: porcentaje de descuento (ej: 10 = 10%)
        BigDecimal descuentoPorcentaje = parametros != null && !parametros.isEmpty()
                ? new BigDecimal(parametros.get(0).toString())
                : BigDecimal.ZERO;

        BigDecimal descuento = subtotal.multiply(descuentoPorcentaje.divide(new BigDecimal("100")));
        BigDecimal subtotalConDescuento = subtotal.subtract(descuento);
        BigDecimal impuesto = subtotalConDescuento.multiply(IVA);

        return subtotalConDescuento.add(impuesto).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getTipo() {
        return "DESCUENTO";
    }
}