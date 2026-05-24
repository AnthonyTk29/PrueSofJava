package com.banking.cuentas.application.service;

import com.banking.cuentas.application.dto.MovimientoCreateRequest;
import com.banking.cuentas.application.dto.MovimientoResponse;
import com.banking.cuentas.application.mapper.MovimientoMapper;
import com.banking.cuentas.domain.entity.Cuenta;
import com.banking.cuentas.domain.entity.Movimiento;
import com.banking.cuentas.domain.exception.DomainException;
import com.banking.cuentas.domain.exception.ResourceNotFoundException;
import com.banking.cuentas.domain.exception.SaldoNoDisponibleException;
import com.banking.cuentas.domain.repository.CuentaRepository;
import com.banking.cuentas.domain.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    private MovimientoServiceImpl movimientoService;

    @BeforeEach
    void setUp() {
        movimientoService = new MovimientoServiceImpl(movimientoRepository, cuentaRepository, new MovimientoMapper());
    }

    @Test
    void createDeposito_success() {
        Cuenta cuenta = buildCuenta("478758", new BigDecimal("2000.00"), true);
        MovimientoCreateRequest request = new MovimientoCreateRequest("478758", "DEPOSITO", new BigDecimal("500.00"));

        Movimiento saved = Movimiento.crearDeposito(1, "478758", new BigDecimal("500.00"), new BigDecimal("2500.00"));
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any())).thenReturn(cuenta);
        when(movimientoRepository.save(any())).thenReturn(saved);

        MovimientoResponse response = movimientoService.create(request);

        assertThat(response.tipoMovimiento()).isEqualTo("DEPOSITO");
        assertThat(response.valor()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void createRetiro_success() {
        Cuenta cuenta = buildCuenta("478758", new BigDecimal("2000.00"), true);
        MovimientoCreateRequest request = new MovimientoCreateRequest("478758", "RETIRO", new BigDecimal("-575.00"));

        Movimiento saved = Movimiento.crearRetiro(1, "478758", new BigDecimal("-575.00"), new BigDecimal("1425.00"));
        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any())).thenReturn(cuenta);
        when(movimientoRepository.save(any())).thenReturn(saved);

        MovimientoResponse response = movimientoService.create(request);

        assertThat(response.tipoMovimiento()).isEqualTo("RETIRO");
        assertThat(response.saldo()).isEqualByComparingTo(new BigDecimal("1425.00"));
    }

    @Test
    void createRetiro_saldoInsuficiente_throwsException() {
        Cuenta cuenta = buildCuenta("478758", new BigDecimal("100.00"), true);
        MovimientoCreateRequest request = new MovimientoCreateRequest("478758", "RETIRO", new BigDecimal("-500.00"));

        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));

        assertThatThrownBy(() -> movimientoService.create(request))
                .isInstanceOf(SaldoNoDisponibleException.class);
    }

    @Test
    void create_cuentaInactiva_throwsException() {
        Cuenta cuenta = buildCuenta("478758", new BigDecimal("2000.00"), false);
        MovimientoCreateRequest request = new MovimientoCreateRequest("478758", "DEPOSITO", new BigDecimal("100.00"));

        when(cuentaRepository.findByNumeroCuenta("478758")).thenReturn(Optional.of(cuenta));

        assertThatThrownBy(() -> movimientoService.create(request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("inactiva");
    }

    @Test
    void create_cuentaNotFound_throwsException() {
        when(cuentaRepository.findByNumeroCuenta("000000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movimientoService.create(
                new MovimientoCreateRequest("000000", "DEPOSITO", new BigDecimal("100.00"))))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAll_returnsMovimientos() {
        Movimiento m = Movimiento.crearDeposito(1, "478758", new BigDecimal("100"), new BigDecimal("2100"));
        when(movimientoRepository.findAll()).thenReturn(List.of(m));

        List<MovimientoResponse> result = movimientoService.getAll();

        assertThat(result).hasSize(1);
    }

    private Cuenta buildCuenta(String numeroCuenta, BigDecimal saldo, boolean estado) {
        Cuenta cuenta = new Cuenta();
        try {
            var field = Cuenta.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(cuenta, 1);
        } catch (Exception ignored) {}
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setTipoCuenta("AHORRO");
        cuenta.setSaldoInicial(saldo);
        cuenta.setSaldoDisponible(saldo);
        cuenta.setClienteId(1);
        cuenta.setEstado(estado);
        return cuenta;
    }
}
