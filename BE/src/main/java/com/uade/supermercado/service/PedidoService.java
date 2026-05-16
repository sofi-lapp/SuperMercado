package com.uade.supermercado.service;

import com.uade.supermercado.dto.response.PedidoResponse;
import com.uade.supermercado.exception.RecursoNoEncontradoException;
import com.uade.supermercado.exception.TransicionEstadoInvalidaException;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.HistorialEstadoPedido;
import com.uade.supermercado.model.pedido.Pedido;
import com.uade.supermercado.model.usuario.Administrador;
import com.uade.supermercado.patterns.observer.EmailObservador;
import com.uade.supermercado.patterns.observer.PushObservador;
import com.uade.supermercado.patterns.observer.SMSObservador;
import com.uade.supermercado.patterns.state.EstadoPedidoFactory;
import com.uade.supermercado.repository.HistorialEstadoPedidoRepository;
import com.uade.supermercado.repository.PedidoRepository;
import com.uade.supermercado.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistorialEstadoPedidoRepository historialRepository;
    private final EmailObservador emailObservador;
    private final SMSObservador smsObservador;
    private final PushObservador pushObservador;

    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(PedidoResponse::from)
                .toList();
    }

    public PedidoResponse obtenerPorId(Long id) {
        return PedidoResponse.from(buscarPedido(id));
    }

    public List<PedidoResponse> listarTodos(EstadoPedidoEnum estado) {
        List<Pedido> pedidos = (estado != null)
                ? pedidoRepository.findByEstado(estado)
                : pedidoRepository.findAll();
        return pedidos.stream().map(PedidoResponse::from).toList();
    }

    @Transactional
    public PedidoResponse cambiarEstado(Long pedidoId, EstadoPedidoEnum nuevoEstado, Long adminId) {
        Pedido pedido = buscarPedido(pedidoId);
        rehidratarEstado(pedido);

        EstadoPedidoEnum estadoAnterior = pedido.getEstado();
        ejecutarTransicion(pedido, nuevoEstado);

        registrarHistorial(pedido, estadoAnterior, adminId);
        pedidoRepository.save(pedido);

        return PedidoResponse.from(pedido);
    }

    private void rehidratarEstado(Pedido pedido) {
        pedido.setEstadoActual(EstadoPedidoFactory.crear(pedido.getEstado()));
        pedido.agregarObservador(emailObservador);
        pedido.agregarObservador(smsObservador);
        pedido.agregarObservador(pushObservador);
    }

    private void ejecutarTransicion(Pedido pedido, EstadoPedidoEnum nuevoEstado) {
        switch (nuevoEstado) {
            case PAGADO -> pedido.pagar();
            case ENVIADO -> pedido.enviar();
            case ENTREGADO -> pedido.entregar();
            case CANCELADO -> pedido.cancelar();
            default -> throw new TransicionEstadoInvalidaException(pedido.getEstado(), nuevoEstado.name());
        }
    }

    private void registrarHistorial(Pedido pedido, EstadoPedidoEnum estadoAnterior, Long adminId) {
        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(pedido);
        historial.setEstadoAnterior(estadoAnterior);
        historial.setEstadoNuevo(pedido.getEstado());

        if (adminId != null) {
            usuarioRepository.findById(adminId)
                    .filter(u -> u instanceof Administrador)
                    .map(u -> (Administrador) u)
                    .ifPresent(historial::setUsuarioAdmin);
        }

        historialRepository.save(historial);
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido", id));
    }
}
