package com.banking.cuentas.application.service;

import com.banking.cuentas.application.dto.CuentaCreateRequest;
import com.banking.cuentas.application.dto.CuentaResponse;
import com.banking.cuentas.application.dto.CuentaUpdateRequest;
import com.banking.cuentas.application.mapper.CuentaMapper;
import com.banking.cuentas.domain.entity.Cuenta;
import com.banking.cuentas.domain.exception.DomainException;
import com.banking.cuentas.domain.exception.ResourceNotFoundException;
import com.banking.cuentas.domain.repository.ClienteReadModelRepository;
import com.banking.cuentas.domain.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteReadModelRepository clienteReadModelRepository;
    private final CuentaMapper mapper;

    public CuentaServiceImpl(CuentaRepository cuentaRepository,
                              ClienteReadModelRepository clienteReadModelRepository,
                              CuentaMapper mapper) {
        this.cuentaRepository = cuentaRepository;
        this.clienteReadModelRepository = clienteReadModelRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> getAll() {
        return cuentaRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponse getById(Integer id) {
        return cuentaRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta con Id " + id + " no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponse getByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta con número " + numeroCuenta + " no encontrada"));
    }

    @Override
    @Transactional
    public CuentaResponse create(CuentaCreateRequest request) {
        if (!clienteReadModelRepository.existsByClienteId(request.clienteId()))
            throw new DomainException("El cliente con Id " + request.clienteId() + " no existe");
        if (cuentaRepository.existsByNumeroCuenta(request.numeroCuenta()))
            throw new DomainException("El número de cuenta " + request.numeroCuenta() + " ya existe");
        Cuenta cuenta = mapper.toEntity(request);
        return mapper.toResponse(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional
    public CuentaResponse update(Integer id, CuentaUpdateRequest request) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta con Id " + id + " no encontrada"));
        mapper.updateEntity(cuenta, request);
        return mapper.toResponse(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional
    public CuentaResponse patchEstado(Integer id, boolean estado) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta con Id " + id + " no encontrada"));
        if (estado) cuenta.activar();
        else cuenta.desactivar();
        return mapper.toResponse(cuentaRepository.save(cuenta));
    }

    @Override
    @Transactional
    public CuentaResponse delete(Integer id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta con Id " + id + " no encontrada"));
        CuentaResponse response = mapper.toResponse(cuenta);
        cuentaRepository.deleteById(id);
        return response;
    }
}
