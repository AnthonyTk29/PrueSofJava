package com.banking.cuentas.application.service;

import com.banking.cuentas.application.dto.CuentaCreateRequest;
import com.banking.cuentas.application.dto.CuentaResponse;
import com.banking.cuentas.application.mapper.CuentaMapper;
import com.banking.cuentas.domain.entity.Cuenta;
import com.banking.cuentas.domain.exception.DomainException;
import com.banking.cuentas.domain.exception.ResourceNotFoundException;
import com.banking.cuentas.domain.repository.ClienteReadModelRepository;
import com.banking.cuentas.domain.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteReadModelRepository clienteReadModelRepository;

    private CuentaServiceImpl cuentaService;

    @BeforeEach
    void setUp() {
        cuentaService = new CuentaServiceImpl(cuentaRepository, clienteReadModelRepository, new CuentaMapper());
    }

    @Test
    void create_success() {
        CuentaCreateRequest request = new CuentaCreateRequest(
                "478758", "AHORRO", new BigDecimal("2000.00"), 1, true
        );
        Cuenta saved = buildCuenta(1, "478758", "AHORRO", new BigDecimal("2000.00"), 1);

        when(clienteReadModelRepository.existsByClienteId(1)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("478758")).thenReturn(false);
        when(cuentaRepository.save(any())).thenReturn(saved);

        CuentaResponse response = cuentaService.create(request);

        assertThat(response.numeroCuenta()).isEqualTo("478758");
        assertThat(response.tipoCuenta()).isEqualTo("AHORRO");
        assertThat(response.saldoInicial()).isEqualByComparingTo(new BigDecimal("2000.00"));
    }

    @Test
    void create_clienteNotExists_throwsException() {
        CuentaCreateRequest request = new CuentaCreateRequest(
                "478758", "AHORRO", new BigDecimal("2000.00"), 999, true
        );
        when(clienteReadModelRepository.existsByClienteId(999)).thenReturn(false);

        assertThatThrownBy(() -> cuentaService.create(request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("999");
    }

    @Test
    void create_numeroCuentaDuplicado_throwsException() {
        CuentaCreateRequest request = new CuentaCreateRequest(
                "478758", "AHORRO", new BigDecimal("2000.00"), 1, true
        );
        when(clienteReadModelRepository.existsByClienteId(1)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("478758")).thenReturn(true);

        assertThatThrownBy(() -> cuentaService.create(request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("478758");
    }

    @Test
    void create_tipoCuentaInvalido_throwsException() {
        CuentaCreateRequest request = new CuentaCreateRequest(
                "478758", "INVALIDO", new BigDecimal("2000.00"), 1, true
        );
        when(clienteReadModelRepository.existsByClienteId(1)).thenReturn(true);
        when(cuentaRepository.existsByNumeroCuenta("478758")).thenReturn(false);

        assertThatThrownBy(() -> cuentaService.create(request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("AHORRO");
    }

    @Test
    void getById_notFound_throwsException() {
        when(cuentaRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cuentaService.getById(999))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAll_returnsCuentas() {
        Cuenta c1 = buildCuenta(1, "478758", "AHORRO", new BigDecimal("2000"), 1);
        Cuenta c2 = buildCuenta(2, "225487", "CORRIENTE", new BigDecimal("100"), 2);
        when(cuentaRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CuentaResponse> result = cuentaService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).numeroCuenta()).isEqualTo("478758");
        assertThat(result.get(1).numeroCuenta()).isEqualTo("225487");
    }

    @Test
    void patchEstado_deactivates() {
        Cuenta cuenta = buildCuenta(1, "478758", "AHORRO", new BigDecimal("2000"), 1);
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));
        // Devuelve el mismo objeto modificado (estado cambia en cuenta.desactivar())
        when(cuentaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CuentaResponse response = cuentaService.patchEstado(1, false);

        assertThat(response.estado()).isFalse();
    }

    @Test
    void patchEstado_activates() {
        Cuenta cuenta = buildCuenta(1, "478758", "AHORRO", new BigDecimal("2000"), 1);
        cuenta.desactivar();
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CuentaResponse response = cuentaService.patchEstado(1, true);

        assertThat(response.estado()).isTrue();
    }

    @Test
    void delete_success() {
        Cuenta cuenta = buildCuenta(1, "478758", "AHORRO", new BigDecimal("2000"), 1);
        when(cuentaRepository.findById(1)).thenReturn(Optional.of(cuenta));

        CuentaResponse response = cuentaService.delete(1);

        assertThat(response.numeroCuenta()).isEqualTo("478758");
    }

    private Cuenta buildCuenta(Integer id, String numeroCuenta, String tipoCuenta, BigDecimal saldo, Integer clienteId) {
        Cuenta cuenta = new Cuenta();
        try {
            var field = Cuenta.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(cuenta, id);
        } catch (Exception ignored) {}
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setTipoCuenta(tipoCuenta);
        cuenta.setSaldoInicial(saldo);
        cuenta.setSaldoDisponible(saldo);
        cuenta.setClienteId(clienteId);
        cuenta.setEstado(true);
        return cuenta;
    }
}
