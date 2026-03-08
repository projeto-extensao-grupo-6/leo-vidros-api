package com.project.extension.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "fila.orcamento.pdf";
    public static final String EXCHANGE_NAME = "exchange.leovidros.direct";
    public static final String ROUTING_KEY = "orcamento.gerar";

    @Bean
    public Queue orcamentoQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange leoVidrosExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding orcamentoBinding(Queue orcamentoQueue, DirectExchange leoVidrosExchange) {
        return BindingBuilder.bind(orcamentoQueue).to(leoVidrosExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
