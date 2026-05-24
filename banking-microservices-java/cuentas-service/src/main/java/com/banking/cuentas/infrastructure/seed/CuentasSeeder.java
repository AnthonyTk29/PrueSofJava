package com.banking.cuentas.infrastructure.seed;

import com.banking.cuentas.domain.entity.ClienteReadModel;
import com.banking.cuentas.domain.entity.Cuenta;
import com.banking.cuentas.domain.entity.Movimiento;
import com.banking.cuentas.infrastructure.persistence.ClienteReadModelJpaRepository;
import com.banking.cuentas.infrastructure.persistence.CuentaJpaRepository;
import com.banking.cuentas.infrastructure.persistence.MovimientoJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@Order(1)
public class CuentasSeeder implements ApplicationRunner {

    private final ClienteReadModelJpaRepository clienteReadModelJpaRepository;
    private final CuentaJpaRepository cuentaJpaRepository;
    private final MovimientoJpaRepository movimientoJpaRepository;

    public CuentasSeeder(ClienteReadModelJpaRepository clienteReadModelJpaRepository,
                          CuentaJpaRepository cuentaJpaRepository,
                          MovimientoJpaRepository movimientoJpaRepository) {
        this.clienteReadModelJpaRepository = clienteReadModelJpaRepository;
        this.cuentaJpaRepository = cuentaJpaRepository;
        this.movimientoJpaRepository = movimientoJpaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (cuentaJpaRepository.count() == 0) {
            log.info("Seeding cuentas data...");
            seedClientesReadModel();
            List<Cuenta> cuentas = seedCuentas();
            seedMovimientos(cuentas);
            log.info("Cuentas seed data inserted.");
        }
    }

    private void seedClientesReadModel() {
        ClienteReadModel c1 = new ClienteReadModel();
        c1.setClienteId(1);
        c1.setNombre("Jose Lema");
        c1.setIdentificacion("1234567890");
        c1.setEstado(true);

        ClienteReadModel c2 = new ClienteReadModel();
        c2.setClienteId(2);
        c2.setNombre("Marianela Montalvo");
        c2.setIdentificacion("0987654321");
        c2.setEstado(true);

        ClienteReadModel c3 = new ClienteReadModel();
        c3.setClienteId(3);
        c3.setNombre("Juan Osorio");
        c3.setIdentificacion("1111111111");
        c3.setEstado(true);

        clienteReadModelJpaRepository.saveAll(List.of(c1, c2, c3));
    }

    private List<Cuenta> seedCuentas() {
        Cuenta cuenta1 = buildCuenta("478758", "AHORRO", new BigDecimal("2000.00"), 1);
        Cuenta cuenta2 = buildCuenta("225487", "CORRIENTE", new BigDecimal("100.00"), 2);
        Cuenta cuenta3 = buildCuenta("495878", "AHORRO", BigDecimal.ZERO, 3);
        Cuenta cuenta4 = buildCuenta("496825", "AHORRO", new BigDecimal("540.00"), 2);
        Cuenta cuenta5 = buildCuenta("585545", "CORRIENTE", new BigDecimal("1000.00"), 3);
        return cuentaJpaRepository.saveAll(List.of(cuenta1, cuenta2, cuenta3, cuenta4, cuenta5));
    }

    private void seedMovimientos(List<Cuenta> cuentas) {
        Movimiento m1 = new Movimiento();
        m1.setCuentaId(cuentas.get(0).getId());
        m1.setNumeroCuenta(cuentas.get(0).getNumeroCuenta());
        m1.setFecha(Instant.now());
        m1.setTipoMovimiento("RETIRO");
        m1.setValor(new BigDecimal("-575.00"));
        m1.setSaldo(new BigDecimal("1425.00"));

        Movimiento m2 = new Movimiento();
        m2.setCuentaId(cuentas.get(1).getId());
        m2.setNumeroCuenta(cuentas.get(1).getNumeroCuenta());
        m2.setFecha(Instant.now());
        m2.setTipoMovimiento("DEPOSITO");
        m2.setValor(new BigDecimal("600.00"));
        m2.setSaldo(new BigDecimal("700.00"));

        Movimiento m3 = new Movimiento();
        m3.setCuentaId(cuentas.get(2).getId());
        m3.setNumeroCuenta(cuentas.get(2).getNumeroCuenta());
        m3.setFecha(Instant.now());
        m3.setTipoMovimiento("DEPOSITO");
        m3.setValor(new BigDecimal("150.00"));
        m3.setSaldo(new BigDecimal("150.00"));

        Movimiento m4 = new Movimiento();
        m4.setCuentaId(cuentas.get(3).getId());
        m4.setNumeroCuenta(cuentas.get(3).getNumeroCuenta());
        m4.setFecha(Instant.now());
        m4.setTipoMovimiento("DEPOSITO");
        m4.setValor(new BigDecimal("540.00"));
        m4.setSaldo(new BigDecimal("540.00"));

        movimientoJpaRepository.saveAll(List.of(m1, m2, m3, m4));
    }

    private Cuenta buildCuenta(String numeroCuenta, String tipoCuenta, BigDecimal saldo, Integer clienteId) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setSaldoInicial(saldo);
        cuenta.setSaldoDisponible(saldo);
        cuenta.setClienteId(clienteId);
        cuenta.setEstado(true);
        return cuenta;
    }
}
