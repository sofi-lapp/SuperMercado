package com.uade.supermercado.dto.request;

import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoRequest(

        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoPedidoEnum nuevoEstado
) {
}
