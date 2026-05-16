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
public class EmailObservador implements IObservadorPedido {

    private static final Logger log = LoggerFactory.getLogger(EmailObservador.class);

    private final NotificacionRepository notificacionRepository;

    @Override
    public void actualizar(Pedido pedido, EstadoPedidoEnum nuevoEstado) {
        String mensaje = construirMensaje(pedido, nuevoEstado);
        log.info("[EMAIL] Para: {} | {}", pedido.getCliente().getEmail(), mensaje);
        persistirNotificacion(pedido, mensaje);
    }

    private String construirMensaje(Pedido pedido, EstadoPedidoEnum nuevoEstado) {
        return String.format("Hola %s, tu pedido #%d cambió al estado: %s",
                pedido.getCliente().getNombre(), pedido.getId(), nuevoEstado);
    }

    private void persistirNotificacion(Pedido pedido, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(pedido.getCliente());
        notificacion.setPedido(pedido);
        notificacion.setTipo(TipoNotificacion.EMAIL);
        notificacion.setMensaje(mensaje);
        notificacion.setEnviada(true);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }
}
