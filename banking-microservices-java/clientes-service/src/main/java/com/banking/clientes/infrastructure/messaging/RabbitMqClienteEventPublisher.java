package com.banking.clientes.infrastructure.messaging;

import com.banking.clientes.application.event.ClienteEventPayload;
import com.banking.clientes.application.event.ClienteEventPublisher;
import com.banking.clientes.infrastructure.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqClienteEventPublisher implements ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqClienteEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishCreated(ClienteEventPayload payload) {
        log.info("Publishing ClienteCreado event for clienteId={}", payload.data().clienteId());
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY_CREATED, payload);
    }

    @Override
    public void publishUpdated(ClienteEventPayload payload) {
        log.info("Publishing ClienteActualizado event for clienteId={}", payload.data().clienteId());
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY_UPDATED, payload);
    }

    @Override
    public void publishDeleted(ClienteEventPayload payload) {
        log.info("Publishing ClienteEliminado event for clienteId={}", payload.data().clienteId());
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY_DELETED, payload);
    }
}
