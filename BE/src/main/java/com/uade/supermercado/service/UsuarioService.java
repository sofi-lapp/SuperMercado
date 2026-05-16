package com.uade.supermercado.service;

import com.uade.supermercado.exception.RecursoNoEncontradoException;
import com.uade.supermercado.model.usuario.Usuario;
import com.uade.supermercado.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario con email=" + email));
    }
}
