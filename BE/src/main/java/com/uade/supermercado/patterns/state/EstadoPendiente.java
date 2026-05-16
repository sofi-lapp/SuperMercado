package com.uade.supermercado.patterns.state;

import com.uade.supermercado.exception.TransicionEstadoInvalidaException;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;

public class EstadoPendiente implements EstadoPedido {

    @Override
    public void pagar(Pedido pedido) {
        pedido.setEstado(EstadoPedidoEnum.PAGADO);
        pedido.setEstadoActual(new EstadoPagado());
        pedido.notificarObservadores();
    }

    @Override
    public void enviar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.PENDIENTE, "enviar");
    }

    @Override
    public void entregar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.PENDIENTE, "entregar");
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.setEstado(EstadoPedidoEnum.CANCELADO);
        pedido.setEstadoActual(new EstadoCancelado());
        pedido.notificarObservadores();
    }

    @Override
    public EstadoPedidoEnum getNombre() {
        return EstadoPedidoEnum.PENDIENTE;
    }
}
