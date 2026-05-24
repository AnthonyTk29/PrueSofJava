package com.banking.clientes.api.controller;

import com.banking.clientes.application.dto.ClienteCreateRequest;
import com.banking.clientes.application.dto.ClienteResponse;
import com.banking.clientes.application.dto.ClienteUpdateRequest;
import com.banking.clientes.application.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes")
public class ClientesController {

    private final ClienteService clienteService;

    public ClientesController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    public List<ClienteResponse> getAll() {
        return clienteService.getAll();
    }

    @GetMapping("/{clienteId}")
    @Operation(summary = "Obtener cliente por ID")
    public ClienteResponse getById(@PathVariable Integer clienteId) {
        return clienteService.getById(clienteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear nuevo cliente")
    public ClienteResponse create(@Valid @RequestBody ClienteCreateRequest request) {
        return clienteService.create(request);
    }

    @PutMapping("/{clienteId}")
    @Operation(summary = "Actualizar cliente")
    public ClienteResponse update(
            @PathVariable Integer clienteId,
            @Valid @RequestBody ClienteUpdateRequest request) {
        return clienteService.update(clienteId, request);
    }

    @PatchMapping("/{clienteId}/estado")
    @Operation(summary = "Cambiar estado del cliente")
    public ClienteResponse patchEstado(
            @PathVariable Integer clienteId,
            @RequestParam boolean estado) {
        return clienteService.patchEstado(clienteId, estado);
    }

    @DeleteMapping("/{clienteId}")
    @Operation(summary = "Eliminar cliente")
    public ClienteResponse delete(@PathVariable Integer clienteId) {
        return clienteService.delete(clienteId);
    }
}
