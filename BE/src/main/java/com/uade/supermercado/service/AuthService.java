package com.uade.supermercado.service;

import com.uade.supermercado.dto.request.LoginRequest;
import com.uade.supermercado.dto.request.RegisterRequest;
import com.uade.supermercado.dto.response.AuthResponse;
import com.uade.supermercado.exception.CredencialesInvalidasException;
import com.uade.supermercado.model.carrito.Carrito;
import com.uade.supermercado.model.usuario.Cliente;
import com.uade.supermercado.model.usuario.Usuario;
import com.uade.supermercado.repository.CarritoRepository;
import com.uade.supermercado.repository.UsuarioRepository;
import com.uade.supermercado.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Cliente cliente = crearCliente(request);
        usuarioRepository.save(cliente);

        crearCarritoVacio(cliente);

        String token = jwtUtil.generarToken(cliente);
        return construirAuthResponse(token, cliente);
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }

        String token = jwtUtil.generarToken(usuario);
        return construirAuthResponse(token, usuario);
    }

    public void logout(String token) {
        redisTemplate.opsForValue().set("blacklist:" + token, "1", 24, TimeUnit.HOURS);
    }

    private Cliente crearCliente(RegisterRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setApellido(request.apellido());
        cliente.setEmail(request.email());
        cliente.setPasswordHash(passwordEncoder.encode(request.password()));
        return cliente;
    }

    private void crearCarritoVacio(Cliente cliente) {
        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        carritoRepository.save(carrito);
    }

    private AuthResponse construirAuthResponse(String token, Usuario usuario) {
        AuthResponse.UsuarioInfo info = new AuthResponse.UsuarioInfo(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol()
        );
        return new AuthResponse(token, info);
    }
}
