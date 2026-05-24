package com.banking.cuentas.domain.entity;

import com.banking.cuentas.domain.exception.DomainException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@NoArgsConstructor
public class Movimiento extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cuenta_id", nullable = false)
    private Integer cuentaId;

    @Column(name = "numero_cuenta", nullable = false, length = 50)
    private String numeroCuenta;

    @Column(nullable = false)
    private Instant fecha;

    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(precision = 18, scale = 2, nullable = false)
    private BigDecimal saldo;

    public static Movimiento crearDeposito(Integer cuentaId, String numeroCuenta,
                                            BigDecimal valor, BigDecimal saldoResultante) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new DomainException("El valor del depósito debe ser positivo");
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setNumeroCuenta(numeroCuenta);
        m.setTipoMovimiento("DEPOSITO");
        m.setValor(valor);
        m.setSaldo(saldoResultante);
        m.setFecha(Instant.now());
        return m;
    }

    public static Movimiento crearRetiro(Integer cuentaId, String numeroCuenta,
                                          BigDecimal valor, BigDecimal saldoResultante) {
        if (valor.compareTo(BigDecimal.ZERO) >= 0)
            throw new DomainException("El valor del retiro debe ser negativo");
        Movimiento m = new Movimiento();
        m.setCuentaId(cuentaId);
        m.setNumeroCuenta(numeroCuenta);
        m.setTipoMovimiento("RETIRO");
        m.setValor(valor);
        m.setSaldo(saldoResultante);
        m.setFecha(Instant.now());
        return m;
    }
}
