package com.uade.supermercado.model.categoria;

import com.uade.supermercado.model.producto.Producto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre_id")
    private Categoria categoriaPadre;

    //hace un join con la tabla categorias
    @OneToMany(mappedBy = "categoriaPadre", fetch = FetchType.LAZY)
    private List<Categoria> subcategorias = new ArrayList<>();

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();

    private boolean activa = true;

    //compara si la lista de subcategorias esta vacia
    public boolean tieneSubcategorias() {
        return !subcategorias.isEmpty();
    }
}
