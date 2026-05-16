package com.uade.supermercado.controller;

import com.uade.supermercado.model.notificacion.Notificacion;
import com.uade.supermercado.model.usuario.Usuario;
import com.uade.supermercado.repository.UsuarioRepository;
import com.uade.supermercado.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<Notificacion>> obtener(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(notificacionService.obtenerPorUsuario(usuario.getId()));
    }
}
