package com.banking.cuentas.application.service;

import com.banking.cuentas.application.dto.ReporteResponse;
import com.banking.cuentas.domain.entity.ClienteReadModel;
import com.banking.cuentas.domain.entity.Cuenta;
import com.banking.cuentas.domain.entity.Movimiento;
import com.banking.cuentas.domain.exception.ResourceNotFoundException;
import com.banking.cuentas.domain.repository.ClienteReadModelRepository;
import com.banking.cuentas.domain.repository.CuentaRepository;
import com.banking.cuentas.domain.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ClienteReadModelRepository clienteReadModelRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public ReporteServiceImpl(ClienteReadModelRepository clienteReadModelRepository,
                               CuentaRepository cuentaRepository,
                               MovimientoRepository movimientoRepository) {
        this.clienteReadModelRepository = clienteReadModelRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteResponse generate(Integer clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        ClienteReadModel cliente = clienteReadModelRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con Id " + clienteId + " no encontrado"));

        Instant inicio = fechaInicio.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant fin = fechaFin.atTime(23, 59, 59, 999_999_999).atOffset(ZoneOffset.UTC).toInstant();

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        List<ReporteResponse.CuentaReporte> cuentasReporte = cuentas.stream()
                .map(cuenta -> {
                    List<Movimiento> movimientos = movimientoRepository
                            .findByCuentaIdAndFechaBetween(cuenta.getId(), inicio, fin);

                    List<ReporteResponse.MovimientoReporte> movimientosReporte = movimientos.stream()
                            .map(m -> new ReporteResponse.MovimientoReporte(
                                    m.getFecha(), m.getTipoMovimiento(), m.getValor(), m.getSaldo()))
                            .toList();

                    return new ReporteResponse.CuentaReporte(
                            cuenta.getNumeroCuenta(),
                            cuenta.getTipoCuenta(),
                            cuenta.getSaldoInicial(),
                            cuenta.getSaldoDisponible(),
                            cuenta.isEstado(),
                            movimientosReporte
                    );
                })
                .toList();

        return new ReporteResponse(clienteId, cliente.getNombre(), inicio, fin, cuentasReporte);
    }
}
