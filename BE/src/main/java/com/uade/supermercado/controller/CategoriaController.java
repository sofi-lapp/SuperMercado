package com.uade.supermercado.controller;

import com.uade.supermercado.dto.request.CategoriaRequest;
import com.uade.supermercado.dto.response.CategoriaResponse;
import com.uade.supermercado.dto.response.ProductoResponse;
import com.uade.supermercado.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarArbol() {
        return ResponseEntity.ok(categoriaService.listarArbol());
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<ProductoResponse>> obtenerProductos(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerProductosDeCategoriaConHijos(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.actualizar(id, request));
    }
}
