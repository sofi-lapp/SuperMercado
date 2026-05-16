package com.uade.supermercado.dto.response;

import com.uade.supermercado.model.categoria.Categoria;

import java.util.List;

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        Long categoriaPadreId,
        boolean activa,
        List<CategoriaResponse> subcategorias
) {
    public static CategoriaResponse from(Categoria c) {
        List<CategoriaResponse> subs = c.getSubcategorias().stream()
                .filter(Categoria::isActiva)
                .map(CategoriaResponse::from)
                .toList();

        return new CategoriaResponse(
                c.getId(),
                c.getNombre(),
                c.getDescripcion(),
                c.getCategoriaPadre() != null ? c.getCategoriaPadre().getId() : null,
                c.isActiva(),
                subs
        );
    }
}
