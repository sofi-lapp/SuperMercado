package com.uade.supermercado.repository;

import com.uade.supermercado.model.producto.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Page<Producto> findByActivoTrue(Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Producto> findByActivoTrueAndNombreContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.categoria.id = :categoriaId")
    Page<Producto> findByActivoTrueAndCategoriaId(@Param("categoriaId") Long categoriaId, Pageable pageable);

    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.categoria.id IN :categoriaIds")
    List<Producto> findByActivoTrueAndCategoriaIdIn(@Param("categoriaIds") List<Long> categoriaIds);
}
