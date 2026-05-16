package com.uade.supermercado.patterns.strategy;

import java.math.BigDecimal;

public interface MetodoPago {

    ResultadoPago procesarPago(BigDecimal monto, String referenciaPedido);

    MetodoPagoEnum getTipo();
}
