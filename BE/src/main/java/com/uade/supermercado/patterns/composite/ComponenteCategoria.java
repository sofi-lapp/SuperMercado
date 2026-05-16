package com.uade.supermercado.patterns.composite;

import com.uade.supermercado.model.producto.Producto;

import java.util.List;

public interface ComponenteCategoria {

    String getNombre();

    void mostrar(int nivel);

    List<Producto> getProductos();
}
