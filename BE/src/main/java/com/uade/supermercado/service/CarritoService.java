package com.uade.supermercado.service;

import com.uade.supermercado.dto.request.CarritoItemRequest;
import com.uade.supermercado.dto.request.ModificarCantidadRequest;
import com.uade.supermercado.dto.response.CarritoResponse;
import com.uade.supermercado.exception.RecursoNoEncontradoException;
import com.uade.supermercado.exception.StockInsuficienteException;
import com.uade.supermercado.model.carrito.Carrito;
import com.uade.supermercado.model.carrito.CarritoItem;
import com.uade.supermercado.model.producto.Producto;
import com.uade.supermercado.repository.CarritoItemRepository;
import com.uade.supermercado.repository.CarritoRepository;
import com.uade.supermercado.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;

    public CarritoResponse obtenerCarrito(Long clienteId) {
        Carrito carrito = buscarCarrito(clienteId);
        return mapearCarrito(carrito);
    }

    @Transactional
    public CarritoResponse agregarItem(Long clienteId, CarritoItemRequest request) {
        Carrito carrito = buscarCarrito(clienteId);
        Producto producto = buscarProducto(request.productoId());

        validarStock(producto, request.cantidad());

        Optional<CarritoItem> itemExistente = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), producto.getId());

        if (itemExistente.isPresent()) {
            incrementarCantidad(itemExistente.get(), request.cantidad(), producto);
        } else {
            agregarNuevoItem(carrito, producto, request.cantidad());
        }

        return mapearCarrito(carritoRepository.save(carrito));
    }

    @Transactional
    public CarritoResponse modificarItem(Long clienteId, Long productoId, ModificarCantidadRequest request) {
        Carrito carrito = buscarCarrito(clienteId);

        if (request.cantidad() == 0) {
            carritoItemRepository.deleteByCarritoIdAndProductoId(carrito.getId(), productoId);
        } else {
            CarritoItem item = carritoItemRepository
                    .findByCarritoIdAndProductoId(carrito.getId(), productoId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Ítem del carrito con productoId=" + productoId));

            Producto producto = buscarProducto(productoId);
            validarStock(producto, request.cantidad());
            item.setCantidad(request.cantidad());
            carritoItemRepository.save(item);
        }

        return mapearCarrito(carritoRepository.findById(carrito.getId()).orElseThrow());
    }

    @Transactional
    public CarritoResponse eliminarItem(Long clienteId, Long productoId) {
        Carrito carrito = buscarCarrito(clienteId);
        carritoItemRepository.deleteByCarritoIdAndProductoId(carrito.getId(), productoId);
        return mapearCarrito(carritoRepository.findById(carrito.getId()).orElseThrow());
    }

    @Transactional
    public void vaciarCarrito(Long clienteId) {
        Carrito carrito = buscarCarrito(clienteId);
        carrito.vaciar();
        carritoRepository.save(carrito);
    }

    private void validarStock(Producto producto, int cantidad) {
        if (!producto.tieneStock(cantidad)) {
            throw new StockInsuficienteException(producto.getNombre(), producto.getStock(), cantidad);
        }
    }

    private void incrementarCantidad(CarritoItem item, int cantidad, Producto producto) {
        int nuevaCantidad = item.getCantidad() + cantidad;
        validarStock(producto, nuevaCantidad);
        item.setCantidad(nuevaCantidad);
        carritoItemRepository.save(item);
    }

    private void agregarNuevoItem(Carrito carrito, Producto producto, int cantidad) {
        CarritoItem item = new CarritoItem();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(producto.getPrecio());
        carrito.getItems().add(item);
    }

    private CarritoResponse mapearCarrito(Carrito carrito) {
        List<CarritoResponse.ItemResponse> items = carrito.getItems().stream()
                .map(i -> new CarritoResponse.ItemResponse(
                        i.getId(),
                        i.getProducto().getId(),
                        i.getProducto().getNombre(),
                        i.getProducto().getImagenUrl(),
                        i.getCantidad(),
                        i.getPrecioUnitario(),
                        i.calcularSubtotal()
                ))
                .toList();
        return new CarritoResponse(carrito.getId(), items, carrito.calcularTotal());
    }

    private Carrito buscarCarrito(Long clienteId) {
        return carritoRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito para clienteId=" + clienteId));
    }

    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }
}
