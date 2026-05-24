package com.banking.clientes.application.service;

import com.banking.clientes.application.dto.ClienteCreateRequest;
import com.banking.clientes.application.dto.ClienteResponse;
import com.banking.clientes.application.dto.ClienteUpdateRequest;

import java.util.List;

public interface ClienteService {

    List<ClienteResponse> getAll();

    ClienteResponse getById(Integer id);

    ClienteResponse create(ClienteCreateRequest request);

    ClienteResponse update(Integer id, ClienteUpdateRequest request);

    ClienteResponse patchEstado(Integer id, boolean estado);

    ClienteResponse delete(Integer id);
}
