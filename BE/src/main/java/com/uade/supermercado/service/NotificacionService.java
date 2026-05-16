package com.uade.supermercado.service;

import com.uade.supermercado.model.notificacion.Notificacion;
import com.uade.supermercado.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public List<Notificacion> obtenerPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByIdDesc(usuarioId);
    }
}
