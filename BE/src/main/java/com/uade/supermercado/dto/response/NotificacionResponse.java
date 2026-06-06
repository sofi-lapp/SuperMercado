package com.uade.supermercado.dto.response;

import com.uade.supermercado.model.notificacion.Notificacion;
import com.uade.supermercado.model.notificacion.TipoNotificacion;

import java.time.LocalDateTime;

public record NotificacionResponse(
        Long id,
        TipoNotificacion tipo,
        String mensaje,
        boolean enviada,
        LocalDateTime fechaEnvio,
        Long pedidoId
) {
    public static NotificacionResponse from(Notificacion n) {
        return new NotificacionResponse(
                n.getId(),
                n.getTipo(),
                n.getMensaje(),
                n.isEnviada(),
                n.getFechaEnvio(),
                n.getPedido() != null ? n.getPedido().getId() : null
        );
    }
}
