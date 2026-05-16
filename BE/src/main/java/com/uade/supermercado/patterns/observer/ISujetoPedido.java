package com.uade.supermercado.patterns.observer;

public interface ISujetoPedido {

    void agregarObservador(IObservadorPedido observador);

    void eliminarObservador(IObservadorPedido observador);

    void notificarObservadores();
}
