package com.banking.customers.infrastructure.messaging;

import com.banking.customers.application.event.CustomerEventPayload;
import com.banking.customers.application.event.CustomerEventPublisher;
import com.banking.customers.infrastructure.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqCustomerEventPublisher implements CustomerEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqCustomerEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishCreated(CustomerEventPayload payload) {
        log.info("Publishing CustomerCreated event for clientId={}", payload.data().clientId());
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY_CREATED, payload);
    }

    @Override
    public void publishUpdated(CustomerEventPayload payload) {
        log.info("Publishing CustomerUpdated event for clientId={}", payload.data().clientId());
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY_UPDATED, payload);
    }

    @Override
    public void publishDeleted(CustomerEventPayload payload) {
        log.info("Publishing CustomerDeleted event for clientId={}", payload.data().clientId());
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY_DELETED, payload);
    }
}
