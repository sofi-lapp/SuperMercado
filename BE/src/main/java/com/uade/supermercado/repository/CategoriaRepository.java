package com.uade.supermercado.repository;

import com.uade.supermercado.model.categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByCategoriaPadreIsNullAndActivaTrue();

    List<Categoria> findByCategoriaPadreIdAndActivaTrue(Long padreId);
}
