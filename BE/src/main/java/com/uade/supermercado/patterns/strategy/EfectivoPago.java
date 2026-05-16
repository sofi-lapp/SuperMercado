package com.uade.supermercado.patterns.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class EfectivoPago implements MetodoPago {

    @Override
    public ResultadoPago procesarPago(BigDecimal monto, String referenciaPedido) {
        String referencia = "EFE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new ResultadoPago(true, referencia,
                "Pago en efectivo registrado. Referencia: " + referencia, MetodoPagoEnum.EFECTIVO);
    }

    @Override
    public MetodoPagoEnum getTipo() {
        return MetodoPagoEnum.EFECTIVO;
    }
}
