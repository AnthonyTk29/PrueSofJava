package com.banking.clientes.domain.entity;

import com.banking.clientes.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Integer clienteId;

    @Column(nullable = false, length = 255)
    private String contrasena;

    @Column(nullable = false)
    private boolean estado = true;

    public void validate() {
        if (getNombre() == null || getNombre().isBlank())
            throw new DomainException("El nombre es requerido");
        if (getIdentificacion() == null || getIdentificacion().isBlank())
            throw new DomainException("La identificación es requerida");
        if (getDireccion() == null || getDireccion().isBlank())
            throw new DomainException("La dirección es requerida");
        if (getTelefono() == null || getTelefono().isBlank())
            throw new DomainException("El teléfono es requerido");
        if (getEdad() < 0)
            throw new DomainException("La edad no puede ser negativa");
        if (contrasena == null || contrasena.isBlank())
            throw new DomainException("La contraseña es requerida");
    }

    public void disable() {
        this.estado = false;
    }

    public void enable() {
        this.estado = true;
    }
}
