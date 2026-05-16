package com.uade.supermercado.service;

import com.uade.supermercado.dto.request.CategoriaRequest;
import com.uade.supermercado.dto.response.CategoriaResponse;
import com.uade.supermercado.dto.response.ProductoResponse;
import com.uade.supermercado.exception.RecursoNoEncontradoException;
import com.uade.supermercado.model.categoria.Categoria;
import com.uade.supermercado.model.producto.Producto;
import com.uade.supermercado.patterns.composite.CategoriaCompuesta;
import com.uade.supermercado.patterns.composite.CategoriaHoja;
import com.uade.supermercado.patterns.composite.ComponenteCategoria;
import com.uade.supermercado.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> listarArbol() {
        return categoriaRepository.findByCategoriaPadreIsNullAndActivaTrue().stream()
                .map(CategoriaResponse::from)
                .toList();
    }

    public List<ProductoResponse> obtenerProductosDeCategoriaConHijos(Long categoriaId) {
        Categoria categoria = buscarCategoria(categoriaId);
        ComponenteCategoria componente = construirComposite(categoria);

        return componente.getProductos().stream()
                .map(ProductoResponse::from)
                .toList();
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());

        if (request.categoriaPadreId() != null) {
            Categoria padre = buscarCategoria(request.categoriaPadreId());
            categoria.setCategoriaPadre(padre);
        }

        return CategoriaResponse.from(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarCategoria(id);
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());

        if (request.categoriaPadreId() != null) {
            Categoria padre = buscarCategoria(request.categoriaPadreId());
            categoria.setCategoriaPadre(padre);
        }

        return CategoriaResponse.from(categoriaRepository.save(categoria));
    }

    private ComponenteCategoria construirComposite(Categoria categoria) {
        if (categoria.tieneSubcategorias()) {
            CategoriaCompuesta compuesta = new CategoriaCompuesta(categoria.getNombre());
            categoria.getSubcategorias().stream()
                    .filter(Categoria::isActiva)
                    .map(this::construirComposite)
                    .forEach(compuesta::agregar);
            return compuesta;
        }
        return new CategoriaHoja(categoria.getNombre(), categoria.getProductos().stream()
                .filter(Producto::isActivo)
                .toList());
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }
}
