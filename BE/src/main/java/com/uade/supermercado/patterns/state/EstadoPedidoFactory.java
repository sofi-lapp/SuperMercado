package com.uade.supermercado.patterns.state;

import com.uade.supermercado.model.pedido.EstadoPedidoEnum;

public class EstadoPedidoFactory {

    public static EstadoPedido crear(EstadoPedidoEnum estado) {
        return switch (estado) {
            case PENDIENTE -> new EstadoPendiente();
            case PAGADO -> new EstadoPagado();
            case ENVIADO -> new EstadoEnviado();
            case ENTREGADO -> new EstadoEntregado();
            case CANCELADO -> new EstadoCancelado();
        };
    }

    private EstadoPedidoFactory() {}
}
