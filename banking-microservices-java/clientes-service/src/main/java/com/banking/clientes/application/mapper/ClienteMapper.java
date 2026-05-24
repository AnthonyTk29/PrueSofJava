package com.banking.clientes.application.mapper;

import com.banking.clientes.application.dto.ClienteCreateRequest;
import com.banking.clientes.application.dto.ClienteResponse;
import com.banking.clientes.application.dto.ClienteUpdateRequest;
import com.banking.clientes.application.event.ClienteEventPayload;
import com.banking.clientes.domain.entity.Cliente;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClienteMapper {

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.isEstado()
        );
    }

    public Cliente toEntity(ClienteCreateRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setGenero(request.genero());
        cliente.setEdad(request.edad());
        cliente.setIdentificacion(request.identificacion());
        cliente.setDireccion(request.direccion());
        cliente.setTelefono(request.telefono());
        cliente.setContrasena(request.contrasena());
        cliente.setEstado(request.estado());
        return cliente;
    }

    public void updateEntity(Cliente cliente, ClienteUpdateRequest request) {
        cliente.setNombre(request.nombre());
        cliente.setGenero(request.genero());
        cliente.setEdad(request.edad());
        cliente.setIdentificacion(request.identificacion());
        cliente.setDireccion(request.direccion());
        cliente.setTelefono(request.telefono());
        if (request.contrasena() != null && !request.contrasena().isBlank()) {
            cliente.setContrasena(request.contrasena());
        }
        cliente.setEstado(request.estado());
    }

    public ClienteEventPayload toEventPayload(Cliente cliente, String eventType) {
        return new ClienteEventPayload(
                eventType,
                Instant.now(),
                new ClienteEventPayload.ClienteData(
                        cliente.getClienteId(),
                        cliente.getNombre(),
                        cliente.getIdentificacion(),
                        cliente.isEstado()
                )
        );
    }
}
