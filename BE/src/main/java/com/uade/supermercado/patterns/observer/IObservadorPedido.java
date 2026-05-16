package com.uade.supermercado.patterns.observer;

import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;

public interface IObservadorPedido {

    void actualizar(Pedido pedido, EstadoPedidoEnum nuevoEstado);
}
