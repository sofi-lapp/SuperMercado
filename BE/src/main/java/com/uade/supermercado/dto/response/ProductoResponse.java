package com.uade.supermercado.dto.response;

import com.uade.supermercado.model.producto.Producto;
import com.uade.supermercado.model.producto.UnidadMedida;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        UnidadMedida unidad,
        BigDecimal peso,
        String imagenUrl,
        Long categoriaId,
        String categoriaNombre,
        boolean activo,
        LocalDateTime fechaAlta
) {
    public static ProductoResponse from(Producto p) {
        return new ProductoResponse(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock(),
                p.getUnidad(),
                p.getPeso(),
                p.getImagenUrl(),
                p.getCategoria() != null ? p.getCategoria().getId() : null,
                p.getCategoria() != null ? p.getCategoria().getNombre() : null,
                p.isActivo(),
                p.getFechaAlta()
        );
    }
}
