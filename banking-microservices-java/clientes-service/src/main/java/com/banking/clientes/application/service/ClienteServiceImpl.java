package com.banking.clientes.application.service;

import com.banking.clientes.application.dto.ClienteCreateRequest;
import com.banking.clientes.application.dto.ClienteResponse;
import com.banking.clientes.application.dto.ClienteUpdateRequest;
import com.banking.clientes.application.event.ClienteEventPublisher;
import com.banking.clientes.application.mapper.ClienteMapper;
import com.banking.clientes.domain.entity.Cliente;
import com.banking.clientes.domain.exception.ResourceNotFoundException;
import com.banking.clientes.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteEventPublisher eventPublisher;
    private final ClienteMapper mapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository,
                               ClienteEventPublisher eventPublisher,
                               ClienteMapper mapper) {
        this.clienteRepository = clienteRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> getAll() {
        return clienteRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse getById(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con Id " + id + " no encontrado"));
        return mapper.toResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponse create(ClienteCreateRequest request) {
        Cliente cliente = mapper.toEntity(request);
        cliente.validate();
        Cliente saved = clienteRepository.save(cliente);
        eventPublisher.publishCreated(mapper.toEventPayload(saved, "ClienteCreado"));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ClienteResponse update(Integer id, ClienteUpdateRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con Id " + id + " no encontrado"));
        mapper.updateEntity(cliente, request);
        cliente.validate();
        Cliente updated = clienteRepository.save(cliente);
        eventPublisher.publishUpdated(mapper.toEventPayload(updated, "ClienteActualizado"));
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public ClienteResponse patchEstado(Integer id, boolean estado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con Id " + id + " no encontrado"));
        if (estado) cliente.enable();
        else cliente.disable();
        Cliente updated = clienteRepository.save(cliente);
        eventPublisher.publishUpdated(mapper.toEventPayload(updated, "ClienteActualizado"));
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public ClienteResponse delete(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente con Id " + id + " no encontrado"));
        ClienteResponse response = mapper.toResponse(cliente);
        eventPublisher.publishDeleted(mapper.toEventPayload(cliente, "ClienteEliminado"));
        clienteRepository.deleteById(id);
        return response;
    }
}
