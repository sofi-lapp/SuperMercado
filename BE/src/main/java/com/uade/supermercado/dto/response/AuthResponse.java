package com.uade.supermercado.dto.response;

public record AuthResponse(
        String token,
        UsuarioInfo usuario
) {
    public record UsuarioInfo(
            Long id,
            String nombre,
            String apellido,
            String email,
            String rol
    ) {
    }
}
