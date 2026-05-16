package com.uade.supermercado.patterns.strategy;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class TransferenciaPago implements MetodoPago {

    @Override
    public ResultadoPago procesarPago(BigDecimal monto, String referenciaPedido) {
        String referencia = "TRF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new ResultadoPago(true, referencia,
                "Transferencia registrada. Referencia: " + referencia, MetodoPagoEnum.TRANSFERENCIA);
    }

    @Override
    public MetodoPagoEnum getTipo() {
        return MetodoPagoEnum.TRANSFERENCIA;
    }
}
