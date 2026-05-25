package com.banking.accounts.infrastructure.config;

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

    public static final String EXCHANGE_NAME = "customers.exchange";
    public static final String QUEUE_NAME = "accounts.customers.queue";
    public static final String ROUTING_KEY_CREATED = "customer.created";
    public static final String ROUTING_KEY_UPDATED = "customer.updated";
    public static final String ROUTING_KEY_DELETED = "customer.deleted";

    @Bean
    public DirectExchange customersExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue accountsCustomersQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding bindingCreated(Queue accountsCustomersQueue, DirectExchange customersExchange) {
        return BindingBuilder.bind(accountsCustomersQueue).to(customersExchange).with(ROUTING_KEY_CREATED);
    }

    @Bean
    public Binding bindingUpdated(Queue accountsCustomersQueue, DirectExchange customersExchange) {
        return BindingBuilder.bind(accountsCustomersQueue).to(customersExchange).with(ROUTING_KEY_UPDATED);
    }

    @Bean
    public Binding bindingDeleted(Queue accountsCustomersQueue, DirectExchange customersExchange) {
        return BindingBuilder.bind(accountsCustomersQueue).to(customersExchange).with(ROUTING_KEY_DELETED);
    }

    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
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
