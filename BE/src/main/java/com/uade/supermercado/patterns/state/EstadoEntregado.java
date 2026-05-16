package com.uade.supermercado.patterns.state;

import com.uade.supermercado.exception.TransicionEstadoInvalidaException;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;

public class EstadoEntregado implements EstadoPedido {

    @Override
    public void pagar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.ENTREGADO, "pagar");
    }

    @Override
    public void enviar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.ENTREGADO, "enviar");
    }

    @Override
    public void entregar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.ENTREGADO, "entregar");
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.ENTREGADO, "cancelar");
    }

    @Override
    public EstadoPedidoEnum getNombre() {
        return EstadoPedidoEnum.ENTREGADO;
    }
}
