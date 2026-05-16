package com.uade.supermercado.patterns.state;

import com.uade.supermercado.exception.TransicionEstadoInvalidaException;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;

public class EstadoEnviado implements EstadoPedido {

    @Override
    public void pagar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.ENVIADO, "pagar");
    }

    @Override
    public void enviar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.ENVIADO, "enviar");
    }

    @Override
    public void entregar(Pedido pedido) {
        pedido.setEstado(EstadoPedidoEnum.ENTREGADO);
        pedido.setEstadoActual(new EstadoEntregado());
        pedido.notificarObservadores();
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.ENVIADO, "cancelar");
    }

    @Override
    public EstadoPedidoEnum getNombre() {
        return EstadoPedidoEnum.ENVIADO;
    }
}
