package com.uade.supermercado.patterns.strategy;

public record ResultadoPago(
        boolean exitoso,
        String referencia,
        String mensaje,
        MetodoPagoEnum metodoPago
) {
}
