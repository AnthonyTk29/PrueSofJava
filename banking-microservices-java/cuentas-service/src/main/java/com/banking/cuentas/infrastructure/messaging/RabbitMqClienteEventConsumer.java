package com.banking.cuentas.infrastructure.messaging;

import com.banking.cuentas.application.event.ClienteEventPayload;
import com.banking.cuentas.domain.entity.ClienteReadModel;
import com.banking.cuentas.domain.repository.ClienteReadModelRepository;
import com.banking.cuentas.infrastructure.config.RabbitMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqClienteEventConsumer {

    private final ClienteReadModelRepository clienteReadModelRepository;

    public RabbitMqClienteEventConsumer(ClienteReadModelRepository clienteReadModelRepository) {
        this.clienteReadModelRepository = clienteReadModelRepository;
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void handleClienteEvent(ClienteEventPayload payload) {
        log.info("Received event: {} for clienteId={}", payload.eventType(), payload.data().clienteId());
        switch (payload.eventType()) {
            case "ClienteCreado" -> handleClienteCreado(payload);
            case "ClienteActualizado" -> handleClienteActualizado(payload);
            case "ClienteEliminado" -> handleClienteEliminado(payload);
            default -> log.warn("Unknown event type: {}", payload.eventType());
        }
    }

    private void handleClienteCreado(ClienteEventPayload payload) {
        ClienteReadModel model = new ClienteReadModel();
        model.setClienteId(payload.data().clienteId());
        model.setNombre(payload.data().nombre());
        model.setIdentificacion(payload.data().identificacion());
        model.setEstado(payload.data().estado());
        clienteReadModelRepository.save(model);
        log.info("ClienteReadModel created for clienteId={}", payload.data().clienteId());
    }

    private void handleClienteActualizado(ClienteEventPayload payload) {
        ClienteReadModel model = clienteReadModelRepository
                .findByClienteId(payload.data().clienteId())
                .orElse(new ClienteReadModel());
        model.setClienteId(payload.data().clienteId());
        model.setNombre(payload.data().nombre());
        model.setIdentificacion(payload.data().identificacion());
        model.setEstado(payload.data().estado());
        clienteReadModelRepository.save(model);
        log.info("ClienteReadModel updated for clienteId={}", payload.data().clienteId());
    }

    private void handleClienteEliminado(ClienteEventPayload payload) {
        clienteReadModelRepository.findByClienteId(payload.data().clienteId())
                .ifPresent(model -> {
                    model.setEstado(false);
                    clienteReadModelRepository.save(model);
                    log.info("ClienteReadModel soft-deleted for clienteId={}", payload.data().clienteId());
                });
    }
}
