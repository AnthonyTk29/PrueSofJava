package com.banking.cuentas.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE_NAME = "clientes.exchange";
    public static final String QUEUE_NAME = "cuentas.clientes.queue";
    public static final String ROUTING_KEY_CREATED = "cliente.creado";
    public static final String ROUTING_KEY_UPDATED = "cliente.actualizado";
    public static final String ROUTING_KEY_DELETED = "cliente.eliminado";

    @Bean
    public DirectExchange clientesExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue cuentasClientesQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingCreado(Queue cuentasClientesQueue, DirectExchange clientesExchange) {
        return BindingBuilder.bind(cuentasClientesQueue).to(clientesExchange).with(ROUTING_KEY_CREATED);
    }

    @Bean
    public Binding bindingActualizado(Queue cuentasClientesQueue, DirectExchange clientesExchange) {
        return BindingBuilder.bind(cuentasClientesQueue).to(clientesExchange).with(ROUTING_KEY_UPDATED);
    }

    @Bean
    public Binding bindingEliminado(Queue cuentasClientesQueue, DirectExchange clientesExchange) {
        return BindingBuilder.bind(cuentasClientesQueue).to(clientesExchange).with(ROUTING_KEY_DELETED);
    }

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // Usa el tipo del parámetro del método @RabbitListener para deserializar,
        // ignorando el header __TypeId__ que viene del servicio publicador
        converter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
