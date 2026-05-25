package com.banking.customers.application.event;

public interface CustomerEventPublisher {

    void publishCreated(CustomerEventPayload payload);

    void publishUpdated(CustomerEventPayload payload);

    void publishDeleted(CustomerEventPayload payload);
}
