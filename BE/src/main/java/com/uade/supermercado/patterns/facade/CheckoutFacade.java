package com.uade.supermercado.patterns.facade;

import com.uade.supermercado.dto.request.CheckoutRequest;
import com.uade.supermercado.dto.response.PedidoResponse;
import com.uade.supermercado.exception.PagoRechazadoException;
import com.uade.supermercado.exception.RecursoNoEncontradoException;
import com.uade.supermercado.model.carrito.Carrito;
import com.uade.supermercado.model.carrito.CarritoItem;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.pedido.HistorialEstadoPedido;
import com.uade.supermercado.model.pedido.Pedido;
import com.uade.supermercado.model.pedido.PedidoItem;
import com.uade.supermercado.model.usuario.Cliente;
import com.uade.supermercado.patterns.observer.EmailObservador;
import com.uade.supermercado.patterns.observer.PushObservador;
import com.uade.supermercado.patterns.observer.SMSObservador;
import com.uade.supermercado.patterns.state.EstadoPedidoFactory;
import com.uade.supermercado.patterns.strategy.MetodoPago;
import com.uade.supermercado.patterns.strategy.MetodoPagoEnum;
import com.uade.supermercado.patterns.strategy.ResultadoPago;
import com.uade.supermercado.repository.CarritoRepository;
import com.uade.supermercado.repository.ClienteRepository;
import com.uade.supermercado.repository.HistorialEstadoPedidoRepository;
import com.uade.supermercado.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutFacade {

    private final ClienteRepository clienteRepository;
    private final CarritoRepository carritoRepository;
    private final PedidoRepository pedidoRepository;
    private final HistorialEstadoPedidoRepository historialRepository;
    private final List<MetodoPago> metodosDePago;
    private final EmailObservador emailObservador;
    private final SMSObservador smsObservador;
    private final PushObservador pushObservador;

    @Transactional
    public PedidoResponse confirmarCompra(Long clienteId, CheckoutRequest request) {
        Carrito carrito = obtenerCarritoNoVacio(clienteId);
        validarStockDeTodosLosItems(carrito);

        BigDecimal total = carrito.calcularTotal();
        ResultadoPago resultado = procesarPago(request.metodoPago(), total);

        reducirStockDeTodosLosItems(carrito);

        Pedido pedido = crearPedido(carrito, request, resultado);
        transicionarAPagado(pedido);
        agregarItemsAlPedido(pedido, carrito);

        pedidoRepository.save(pedido);

        vaciarCarrito(carrito);
        registrarHistorial(pedido);
        notificarObservadores(pedido);

        return PedidoResponse.from(pedido);
    }

    private Carrito obtenerCarritoNoVacio(Long clienteId) {
        Carrito carrito = carritoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito para clienteId=" + clienteId));
        if (carrito.estaVacio()) {
            throw new IllegalStateException("El carrito está vacío");
        }
        return carrito;
    }

    private void validarStockDeTodosLosItems(Carrito carrito) {
        carrito.getItems().forEach(item ->
                item.getProducto().tieneStock(item.getCantidad())
        );
    }

    private ResultadoPago procesarPago(MetodoPagoEnum tipo, BigDecimal total) {
        MetodoPago metodo = metodosDePago.stream()
                .filter(m -> m.getTipo() == tipo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no soportado: " + tipo));

        ResultadoPago resultado = metodo.procesarPago(total, "PED-" + System.currentTimeMillis());
        if (!resultado.exitoso()) {
            throw new PagoRechazadoException(resultado.mensaje());
        }
        return resultado;
    }

    private void reducirStockDeTodosLosItems(Carrito carrito) {
        carrito.getItems().forEach(item ->
                item.getProducto().reducirStock(item.getCantidad())
        );
    }

    private Pedido crearPedido(Carrito carrito, CheckoutRequest request, ResultadoPago resultado) {
        Cliente cliente = clienteRepository.findById(carrito.getCliente().getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", carrito.getCliente().getId()));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEstado(EstadoPedidoEnum.PENDIENTE);
        pedido.setEstadoActual(EstadoPedidoFactory.crear(EstadoPedidoEnum.PENDIENTE));
        pedido.setMetodoPago(request.metodoPago());
        pedido.setReferenciaPago(resultado.referencia());
        pedido.setDireccionEnvio(request.direccionEnvio());
        pedido.setTotal(carrito.calcularTotal());
        return pedido;
    }

    private void transicionarAPagado(Pedido pedido) {
        pedido.pagar();
    }

    private void agregarItemsAlPedido(Pedido pedido, Carrito carrito) {
        carrito.getItems().forEach(carritoItem -> {
            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setPedido(pedido);
            pedidoItem.setProducto(carritoItem.getProducto());
            pedidoItem.setCantidad(carritoItem.getCantidad());
            pedidoItem.setPrecioUnitario(carritoItem.getPrecioUnitario());
            pedidoItem.setSubtotal(carritoItem.calcularSubtotal());
            pedido.getItems().add(pedidoItem);
        });
    }

    private void vaciarCarrito(Carrito carrito) {
        carrito.vaciar();
        carritoRepository.save(carrito);
    }

    private void registrarHistorial(Pedido pedido) {
        HistorialEstadoPedido historial = new HistorialEstadoPedido();
        historial.setPedido(pedido);
        historial.setEstadoAnterior(EstadoPedidoEnum.PENDIENTE);
        historial.setEstadoNuevo(EstadoPedidoEnum.PAGADO);
        historialRepository.save(historial);
    }

    private void notificarObservadores(Pedido pedido) {
        pedido.agregarObservador(emailObservador);
        pedido.agregarObservador(smsObservador);
        pedido.agregarObservador(pushObservador);
        pedido.notificarObservadores();
    }
}
