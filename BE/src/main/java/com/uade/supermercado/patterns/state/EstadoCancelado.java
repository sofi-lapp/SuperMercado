package com.uade.supermercado.patterns.state;

import com.uade.supermercado.exception.TransicionEstadoInvalidaException;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;

public class EstadoCancelado implements EstadoPedido {

    @Override
    public void pagar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.CANCELADO, "pagar");
    }

    @Override
    public void enviar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.CANCELADO, "enviar");
    }

    @Override
    public void entregar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.CANCELADO, "entregar");
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.CANCELADO, "cancelar");
    }

    @Override
    public EstadoPedidoEnum getNombre() {
        return EstadoPedidoEnum.CANCELADO;
    }
}
