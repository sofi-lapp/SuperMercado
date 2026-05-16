package com.uade.supermercado.patterns.state;

import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;

public interface EstadoPedido {

    void pagar(Pedido pedido);

    void enviar(Pedido pedido);

    void entregar(Pedido pedido);

    void cancelar(Pedido pedido);

    EstadoPedidoEnum getNombre();
}
