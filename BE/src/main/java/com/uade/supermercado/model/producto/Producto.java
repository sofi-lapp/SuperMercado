package com.uade.supermercado.model.producto;

import com.uade.supermercado.exception.StockInsuficienteException;
import com.uade.supermercado.model.categoria.Categoria;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private UnidadMedida unidad;

    @Column(precision = 10, scale = 3)
    private BigDecimal peso;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    private boolean activo = true;

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

    @PrePersist
    protected void onCreate() {
        fechaAlta = LocalDateTime.now();
    }

    public void reducirStock(int cantidad) {
        if (this.stock < cantidad) {
            throw new StockInsuficienteException(this.nombre, this.stock, cantidad);
        }
        this.stock -= cantidad;
    }

    public boolean tieneStock(int cantidad) {
        return this.stock >= cantidad;
    }
}
