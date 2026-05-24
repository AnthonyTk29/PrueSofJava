package com.banking.clientes.application.event;

public interface ClienteEventPublisher {

    void publishCreated(ClienteEventPayload payload);

    void publishUpdated(ClienteEventPayload payload);

    void publishDeleted(ClienteEventPayload payload);
}
