package com.uade.supermercado.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String descripcion,

        Long categoriaPadreId
) {
}
