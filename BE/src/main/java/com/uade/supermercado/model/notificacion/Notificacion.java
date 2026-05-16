package com.uade.supermercado.model.notificacion;

import com.uade.supermercado.model.pedido.Pedido;
import com.uade.supermercado.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoNotificacion tipo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    private boolean enviada = false;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
}
