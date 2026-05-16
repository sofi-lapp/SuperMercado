package com.uade.supermercado.model.usuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ADMIN")//Cuando la columna rol tenga el valor ADMIN crea un objeto de esta clase
@NoArgsConstructor
public class Administrador extends Usuario {
}
