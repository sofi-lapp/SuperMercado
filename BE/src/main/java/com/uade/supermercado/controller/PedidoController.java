package com.uade.supermercado.controller;

import com.uade.supermercado.dto.request.CambioEstadoRequest;
import com.uade.supermercado.dto.response.PedidoResponse;
import com.uade.supermercado.model.pedido.EstadoPedidoEnum;
import com.uade.supermercado.model.usuario.Usuario;
import com.uade.supermercado.repository.UsuarioRepository;
import com.uade.supermercado.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos(@AuthenticationPrincipal UserDetails userDetails) {
        Long clienteId = resolverUserId(userDetails);
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos(
            @RequestParam(required = false) EstadoPedidoEnum estado) {
        return ResponseEntity.ok(pedidoService.listarTodos(estado));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<PedidoResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long adminId = resolverUserId(userDetails);
        return ResponseEntity.ok(pedidoService.cambiarEstado(id, request.nuevoEstado(), adminId));
    }

    private Long resolverUserId(UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }
}
