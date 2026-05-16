package com.uade.supermercado.model.pedido;

import com.uade.supermercado.model.usuario.Cliente;
import com.uade.supermercado.patterns.observer.IObservadorPedido;
import com.uade.supermercado.patterns.observer.ISujetoPedido;
import com.uade.supermercado.patterns.state.EstadoPedido;
import com.uade.supermercado.patterns.strategy.MetodoPagoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
public class Pedido implements ISujetoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedidoEnum estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPagoEnum metodoPago;

    @Column(name = "referencia_pago", length = 255)
    private String referenciaPago;

    @Column(name = "direccion_envio", length = 500)
    private String direccionEnvio;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoItem> items = new ArrayList<>();

    @Transient
    private EstadoPedido estadoActual;

    @Transient
    private List<IObservadorPedido> observadores = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaPedido = LocalDateTime.now();
    }

    public BigDecimal calcularTotal() {
        return items.stream()
                .map(PedidoItem::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public void pagar() {
        estadoActual.pagar(this);
    }

    public void enviar() {
        estadoActual.enviar(this);
    }

    public void entregar() {
        estadoActual.entregar(this);
    }

    public void cancelar() {
        estadoActual.cancelar(this);
    }

    @Override
    public void agregarObservador(IObservadorPedido observador) {
        if (observadores == null) {
            observadores = new ArrayList<>();
        }
        observadores.add(observador);
    }

    @Override
    public void eliminarObservador(IObservadorPedido observador) {
        if (observadores != null) {
            observadores.remove(observador);
        }
    }

    @Override
    public void notificarObservadores() {
        if (observadores == null) return;
        for (IObservadorPedido obs : observadores) {
            obs.actualizar(this, this.estado);
        }
    }
}
