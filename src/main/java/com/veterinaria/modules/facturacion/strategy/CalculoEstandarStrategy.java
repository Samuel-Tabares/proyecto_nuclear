package com.veterinaria.modules.facturacion.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Strategy para cálculo estándar con IVA fijo del 19%
 */
@Component
public class CalculoEstandarStrategy implements CalculoFacturaStrategy {

    private static final BigDecimal IVA = new BigDecimal("0.19");

    @Override
    public BigDecimal calcularTotal(BigDecimal subtotal, List<Object> parametros) {
        BigDecimal impuesto = subtotal.multiply(IVA);
        return subtotal.add(impuesto).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getTipo() {
        return "ESTANDAR";
    }
}