package com.uade.supermercado.dto.request;

import com.uade.supermercado.patterns.strategy.MetodoPagoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(

        @NotNull(message = "El método de pago es obligatorio")
        MetodoPagoEnum metodoPago,

        @NotBlank(message = "La dirección de envío es obligatoria")
        String direccionEnvio
) {
}
