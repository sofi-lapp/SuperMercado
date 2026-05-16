package com.uade.supermercado.patterns.state;

import com.uade.supermercado.exception.TransicionEstadoInvalidaException;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;

public class EstadoPagado implements EstadoPedido {

    @Override
    public void pagar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.PAGADO, "pagar");
    }

    @Override
    public void enviar(Pedido pedido) {
        pedido.setEstado(EstadoPedidoEnum.ENVIADO);
        pedido.setEstadoActual(new EstadoEnviado());
        pedido.notificarObservadores();
    }

    @Override
    public void entregar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.PAGADO, "entregar");
    }

    @Override
    public void cancelar(Pedido pedido) {
        throw new TransicionEstadoInvalidaException(EstadoPedidoEnum.PAGADO, "cancelar");
    }

    @Override
    public EstadoPedidoEnum getNombre() {
        return EstadoPedidoEnum.PAGADO;
    }
}
