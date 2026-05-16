package com.uade.supermercado.patterns.composite;

import com.uade.supermercado.model.producto.Producto;

import java.util.List;

public class CategoriaHoja implements ComponenteCategoria {

    private final String nombre;
    private final List<Producto> productos;

    public CategoriaHoja(String nombre, List<Producto> productos) {
        this.nombre = nombre;
        this.productos = productos;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void mostrar(int nivel) {
        System.out.println("  ".repeat(nivel) + "- " + nombre + " (" + productos.size() + " productos)");
    }

    @Override
    public List<Producto> getProductos() {
        return productos;
    }
}
