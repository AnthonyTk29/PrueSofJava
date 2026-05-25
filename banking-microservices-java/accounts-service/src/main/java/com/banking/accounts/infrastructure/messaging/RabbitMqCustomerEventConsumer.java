package com.banking.accounts.infrastructure.messaging;

import com.banking.accounts.application.event.CustomerEventPayload;
import com.banking.accounts.domain.entity.CustomerReadModel;
import com.banking.accounts.domain.repository.CustomerReadModelRepository;
import com.banking.accounts.infrastructure.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqCustomerEventConsumer {

    private final CustomerReadModelRepository customerReadModelRepository;

    public RabbitMqCustomerEventConsumer(CustomerReadModelRepository customerReadModelRepository) {
        this.customerReadModelRepository = customerReadModelRepository;
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void handleCustomerEvent(CustomerEventPayload payload) {
        log.info("Received event: {} for clientId={}", payload.eventType(), payload.data().clientId());
        switch (payload.eventType()) {
            case "CustomerCreated" -> handleCustomerCreated(payload);
            case "CustomerUpdated" -> handleCustomerUpdated(payload);
            case "CustomerDeleted" -> handleCustomerDeleted(payload);
            default -> log.warn("Unknown event type: {}", payload.eventType());
        }
    }

    private void handleCustomerCreated(CustomerEventPayload payload) {
        CustomerReadModel model = new CustomerReadModel();
        model.setClientId(payload.data().clientId());
        model.setName(payload.data().name());
        model.setIdentification(payload.data().identification());
        model.setActive(payload.data().active());
        customerReadModelRepository.save(model);
        log.info("CustomerReadModel created for clientId={}", payload.data().clientId());
    }

    private void handleCustomerUpdated(CustomerEventPayload payload) {
        CustomerReadModel model = customerReadModelRepository
                .findByClientId(payload.data().clientId())
                .orElse(new CustomerReadModel());
        model.setClientId(payload.data().clientId());
        model.setName(payload.data().name());
        model.setIdentification(payload.data().identification());
        model.setActive(payload.data().active());
        customerReadModelRepository.save(model);
        log.info("CustomerReadModel updated for clientId={}", payload.data().clientId());
    }

    private void handleCustomerDeleted(CustomerEventPayload payload) {
        customerReadModelRepository.findByClientId(payload.data().clientId())
                .ifPresent(model -> {
                    model.setActive(false);
                    customerReadModelRepository.save(model);
                    log.info("CustomerReadModel soft-deleted for clientId={}", payload.data().clientId());
                });
    }
}
