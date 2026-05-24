package com.banking.cuentas.application.service;

import com.banking.cuentas.application.dto.MovimientoCreateRequest;
import com.banking.cuentas.application.dto.MovimientoResponse;
import com.banking.cuentas.application.dto.MovimientoUpdateRequest;
import com.banking.cuentas.application.mapper.MovimientoMapper;
import com.banking.cuentas.domain.entity.Cuenta;
import com.banking.cuentas.domain.entity.Movimiento;
import com.banking.cuentas.domain.exception.DomainException;
import com.banking.cuentas.domain.exception.ResourceNotFoundException;
import com.banking.cuentas.domain.exception.SaldoNoDisponibleException;
import com.banking.cuentas.domain.repository.CuentaRepository;
import com.banking.cuentas.domain.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper mapper;

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository,
                                  CuentaRepository cuentaRepository,
                                  MovimientoMapper mapper) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoResponse> getAll() {
        return movimientoRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoResponse getById(Integer id) {
        return movimientoRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento con Id " + id + " no encontrado"));
    }

    @Override
    @Transactional
    public MovimientoResponse create(MovimientoCreateRequest request) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(request.numeroCuenta())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cuenta con número " + request.numeroCuenta() + " no encontrada"));

        if (!cuenta.isEstado())
            throw new DomainException("La cuenta se encuentra inactiva");

        BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().add(request.valor());

        if (request.valor().compareTo(BigDecimal.ZERO) < 0 && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0)
            throw new SaldoNoDisponibleException("Saldo no disponible para realizar el retiro");

        Movimiento movimiento;
        if (request.valor().compareTo(BigDecimal.ZERO) >= 0) {
            movimiento = Movimiento.crearDeposito(cuenta.getId(), cuenta.getNumeroCuenta(),
                    request.valor(), nuevoSaldo);
        } else {
            movimiento = Movimiento.crearRetiro(cuenta.getId(), cuenta.getNumeroCuenta(),
                    request.valor(), nuevoSaldo);
        }

        cuenta.actualizarSaldo(nuevoSaldo);
        cuentaRepository.save(cuenta);
        Movimiento saved = movimientoRepository.save(movimiento);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public MovimientoResponse update(Integer id, MovimientoUpdateRequest request) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento con Id " + id + " no encontrado"));
        movimiento.setNumeroCuenta(request.numeroCuenta());
        movimiento.setTipoMovimiento(request.tipoMovimiento().toUpperCase());
        movimiento.setValor(request.valor());
        return mapper.toResponse(movimientoRepository.save(movimiento));
    }

    @Override
    @Transactional
    public MovimientoResponse delete(Integer id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento con Id " + id + " no encontrado"));
        MovimientoResponse response = mapper.toResponse(movimiento);
        movimientoRepository.deleteById(id);
        return response;
    }
}
