package com.uade.supermercado.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CarritoResponse(
        Long id,
        List<ItemResponse> items,
        BigDecimal total
) {
    public record ItemResponse(
            Long id,
            Long productoId,
            String productoNombre,
            String imagenUrl,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {
    }
}
