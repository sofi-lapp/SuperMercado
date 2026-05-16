package com.uade.supermercado.patterns.observer;

import com.uade.supermercado.model.notificacion.Notificacion;
import com.uade.supermercado.model.notificacion.TipoNotificacion;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.Pedido;
import com.uade.supermercado.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PushObservador implements IObservadorPedido {

    private static final Logger log = LoggerFactory.getLogger(PushObservador.class);

    private final NotificacionRepository notificacionRepository;

    @Override
    public void actualizar(Pedido pedido, EstadoPedidoEnum nuevoEstado) {
        String mensaje = construirMensaje(pedido, nuevoEstado);
        log.info("[PUSH] Para usuario id={} | {}", pedido.getCliente().getId(), mensaje);
        persistirNotificacion(pedido, mensaje);
    }

    private String construirMensaje(Pedido pedido, EstadoPedidoEnum nuevoEstado) {
        return String.format("Tu pedido #%d fue actualizado a: %s", pedido.getId(), nuevoEstado);
    }

    private void persistirNotificacion(Pedido pedido, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(pedido.getCliente());
        notificacion.setPedido(pedido);
        notificacion.setTipo(TipoNotificacion.PUSH);
        notificacion.setMensaje(mensaje);
        notificacion.setEnviada(true);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }
}
