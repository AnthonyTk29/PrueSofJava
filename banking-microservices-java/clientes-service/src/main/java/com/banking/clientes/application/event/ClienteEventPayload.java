package com.banking.clientes.application.event;

import java.time.Instant;

public record ClienteEventPayload(
        String eventType,
        Instant timestamp,
        ClienteData data
) {
    public record ClienteData(
            Integer clienteId,
            String nombre,
            String identificacion,
            boolean estado
    ) {}
}
