package com.banking.cuentas.domain.entity;

import com.banking.cuentas.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
@NoArgsConstructor
public class Cuenta extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_cuenta", unique = true, nullable = false, length = 50)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false, length = 20)
    private String tipoCuenta;

    @Column(name = "saldo_inicial", precision = 18, scale = 2, nullable = false)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_disponible", precision = 18, scale = 2, nullable = false)
    private BigDecimal saldoDisponible;

    @Column(nullable = false)
    private boolean estado = true;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    public void setNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank())
            throw new DomainException("El número de cuenta es requerido");
        this.numeroCuenta = numeroCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        if (tipoCuenta == null || tipoCuenta.isBlank())
            throw new DomainException("El tipo de cuenta es requerido");
        String normalized = tipoCuenta.toUpperCase();
        if (!normalized.equals("AHORRO") && !normalized.equals("CORRIENTE"))
            throw new DomainException("El tipo de cuenta debe ser AHORRO o CORRIENTE");
        this.tipoCuenta = normalized;
    }

    public void actualizarSaldo(BigDecimal nuevoSaldo) {
        this.saldoDisponible = nuevoSaldo;
    }

    public void activar() {
        this.estado = true;
    }

    public void desactivar() {
        this.estado = false;
    }
}
