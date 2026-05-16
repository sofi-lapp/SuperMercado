package com.uade.supermercado.controller;

import com.uade.supermercado.dto.request.CarritoItemRequest;
import com.uade.supermercado.dto.request.ModificarCantidadRequest;
import com.uade.supermercado.dto.response.CarritoResponse;
import com.uade.supermercado.model.usuario.Usuario;
import com.uade.supermercado.repository.UsuarioRepository;
import com.uade.supermercado.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<CarritoResponse> obtener(@AuthenticationPrincipal UserDetails userDetails) {
        Long clienteId = resolverClienteId(userDetails);
        return ResponseEntity.ok(carritoService.obtenerCarrito(clienteId));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponse> agregar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CarritoItemRequest request) {
        Long clienteId = resolverClienteId(userDetails);
        return ResponseEntity.ok(carritoService.agregarItem(clienteId, request));
    }

    @PutMapping("/items/{productoId}")
    public ResponseEntity<CarritoResponse> modificar(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productoId,
            @Valid @RequestBody ModificarCantidadRequest request) {
        Long clienteId = resolverClienteId(userDetails);
        return ResponseEntity.ok(carritoService.modificarItem(clienteId, productoId, request));
    }

    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<CarritoResponse> eliminarItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productoId) {
        Long clienteId = resolverClienteId(userDetails);
        return ResponseEntity.ok(carritoService.eliminarItem(clienteId, productoId));
    }

    @DeleteMapping
    public ResponseEntity<Void> vaciar(@AuthenticationPrincipal UserDetails userDetails) {
        Long clienteId = resolverClienteId(userDetails);
        carritoService.vaciarCarrito(clienteId);
        return ResponseEntity.noContent().build();
    }

    private Long resolverClienteId(UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }
}
