package com.banking.cuentas.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes_read_model")
@Getter
@Setter
@NoArgsConstructor
public class ClienteReadModel extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cliente_id", unique = true, nullable = false)
    private Integer clienteId;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String identificacion;

    @Column(nullable = false)
    private boolean estado = true;
}
