package com.uade.supermercado.patterns.composite;

import com.uade.supermercado.model.producto.Producto;

import java.util.ArrayList;
import java.util.List;

public class CategoriaCompuesta implements ComponenteCategoria {

    private final String nombre;
    private final List<ComponenteCategoria> hijos = new ArrayList<>();

    public CategoriaCompuesta(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(ComponenteCategoria componente) {
        hijos.add(componente);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void mostrar(int nivel) {
        System.out.println("  ".repeat(nivel) + "+ " + nombre);
        hijos.forEach(hijo -> hijo.mostrar(nivel + 1));
    }

    @Override
    public List<Producto> getProductos() {
        List<Producto> todos = new ArrayList<>();
        hijos.forEach(hijo -> todos.addAll(hijo.getProductos()));
        return todos;
    }

    public List<ComponenteCategoria> getHijos() {
        return hijos;
    }
}
