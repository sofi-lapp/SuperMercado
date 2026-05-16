package com.uade.supermercado.exception;

import com.uade.supermercado.model.pedido.EstadoPedidoEnum;

public class TransicionEstadoInvalidaException extends RuntimeException {

    public TransicionEstadoInvalidaException(EstadoPedidoEnum estadoActual, String accion) {
        super(String.format("No se puede '%s' un pedido en estado '%s'", accion, estadoActual));
    }
}
