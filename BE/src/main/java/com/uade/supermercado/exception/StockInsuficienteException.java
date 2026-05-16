package com.uade.supermercado.exception;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String nombreProducto, int stockDisponible, int cantidadSolicitada) {
        super(String.format("Stock insuficiente para '%s': disponible=%d, solicitado=%d",
                nombreProducto, stockDisponible, cantidadSolicitada));
    }
}
