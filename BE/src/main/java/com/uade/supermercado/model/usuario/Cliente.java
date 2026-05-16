package com.uade.supermercado.model.usuario;

import com.uade.supermercado.model.carrito.Carrito;
import com.uade.supermercado.model.pedido.Pedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENTE") //Cuando la columna rol tenga el valor CLIENTE crea un objeto de esta clase
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Usuario {

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Carrito carrito;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
}
