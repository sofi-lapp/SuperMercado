package com.uade.supermercado.service;

import com.uade.supermercado.dto.request.ProductoRequest;
import com.uade.supermercado.dto.response.ProductoResponse;
import com.uade.supermercado.exception.RecursoNoEncontradoException;
import com.uade.supermercado.model.categoria.Categoria;
import com.uade.supermercado.model.producto.Producto;
import com.uade.supermercado.repository.CategoriaRepository;
import com.uade.supermercado.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ImagenService imagenService;

    public Page<ProductoResponse> listar(String nombre, Long categoriaId, Pageable pageable) {
        if (nombre != null && !nombre.isBlank()) {
            return productoRepository
                    .findByActivoTrueAndNombreContainingIgnoreCase(nombre, pageable)
                    .map(ProductoResponse::from);
        }
        if (categoriaId != null) {
            return productoRepository
                    .findByActivoTrueAndCategoriaId(categoriaId, pageable)
                    .map(ProductoResponse::from);
        }
        return productoRepository.findByActivoTrue(pageable).map(ProductoResponse::from);
    }

    public ProductoResponse obtenerPorId(Long id) {
        return ProductoResponse.from(buscarProducto(id));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request, MultipartFile imagen) {
        Categoria categoria = buscarCategoria(request.categoriaId());
        Producto producto = mapearDesdeRequest(request, categoria);

        if (imagen != null && !imagen.isEmpty()) {
            producto.setImagenUrl(imagenService.subirImagen(imagen));
        }

        return ProductoResponse.from(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request, MultipartFile imagen) {
        Producto producto = buscarProducto(id);
        Categoria categoria = buscarCategoria(request.categoriaId());

        actualizarCampos(producto, request, categoria);

        if (imagen != null && !imagen.isEmpty()) {
            producto.setImagenUrl(imagenService.subirImagen(imagen));
        }

        return ProductoResponse.from(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarProducto(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    private Producto buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }

    private Producto mapearDesdeRequest(ProductoRequest request, Categoria categoria) {
        Producto producto = new Producto();
        actualizarCampos(producto, request, categoria);
        return producto;
    }

    private void actualizarCampos(Producto producto, ProductoRequest request, Categoria categoria) {
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setUnidad(request.unidad());
        producto.setPeso(request.peso());
        producto.setCategoria(categoria);
    }
}
