package com.uade.supermercado.service;

import com.uade.supermercado.dto.response.NotificacionResponse;
import com.uade.supermercado.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Transactional(readOnly = true)
    public List<NotificacionResponse> obtenerPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByIdDesc(usuarioId)
                .stream()
                .map(NotificacionResponse::from)
                .toList();
    }
}
