package com.banking.accounts.application.event;

import java.time.Instant;

public record CustomerEventPayload(
        String eventType,
        Instant timestamp,
        CustomerData data
) {
    public record CustomerData(
            Integer clientId,
            String name,
            String identification,
            boolean active
    ) {}
}
