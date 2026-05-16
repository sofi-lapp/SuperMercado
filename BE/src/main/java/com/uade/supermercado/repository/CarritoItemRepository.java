package com.uade.supermercado.repository;

import com.uade.supermercado.model.carrito.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    Optional<CarritoItem> findByCarritoIdAndProductoId(Long carritoId, Long productoId);

    void deleteByCarritoIdAndProductoId(Long carritoId, Long productoId);
}
