package com.uade.supermercado.dto.response;

import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;
import com.uade.supermercado.patterns.strategy.MetodoPagoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long clienteId,
        String clienteNombre,
        LocalDateTime fechaPedido,
        EstadoPedidoEnum estado,
        BigDecimal total,
        MetodoPagoEnum metodoPago,
        String referenciaPago,
        String direccionEnvio,
        List<ItemResponse> items
) {
    public record ItemResponse(
            Long id,
            Long productoId,
            String productoNombre,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal
    ) {
    }

    public static PedidoResponse from(Pedido p) {
        List<ItemResponse> items = p.getItems().stream()
                .map(i -> new ItemResponse(
                        i.getId(),
                        i.getProducto().getId(),
                        i.getProducto().getNombre(),
                        i.getCantidad(),
                        i.getPrecioUnitario(),
                        i.calcularSubtotal()
                ))
                .toList();

        return new PedidoResponse(
                p.getId(),
                p.getCliente().getId(),
                p.getCliente().getNombre() + " " + p.getCliente().getApellido(),
                p.getFechaPedido(),
                p.getEstado(),
                p.getTotal(),
                p.getMetodoPago(),
                p.getReferenciaPago(),
                p.getDireccionEnvio(),
                items
        );
    }
}
