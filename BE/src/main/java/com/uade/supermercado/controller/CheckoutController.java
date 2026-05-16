package com.uade.supermercado.controller;

import com.uade.supermercado.dto.request.CheckoutRequest;
import com.uade.supermercado.dto.response.PedidoResponse;
import com.uade.supermercado.model.usuario.Usuario;
import com.uade.supermercado.patterns.facade.CheckoutFacade;
import com.uade.supermercado.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutFacade checkoutFacade;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/confirmar")
    public ResponseEntity<PedidoResponse> confirmar(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CheckoutRequest request) {
        Long clienteId = resolverClienteId(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(checkoutFacade.confirmarCompra(clienteId, request));
    }

    private Long resolverClienteId(UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }
}
