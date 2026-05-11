package com.project.extension.integration;

import com.project.extension.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class RabbitMQIntegrationIT extends AbstractIntegrationIT {

    @Autowired RabbitTemplate rabbitTemplate;

    @Test
    void deveRotearMensagemParaFilaDeOrcamento() {
        String payload = "ORC-TEST-001";

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, payload);

        Object recebido = rabbitTemplate.receiveAndConvert(RabbitMQConfig.QUEUE_NAME, 5_000);

        assertNotNull(recebido, "Mensagem não chegou à fila dentro do timeout");
        assertEquals(payload, recebido);
    }

    @Test
    void deveRotearMensagemParaFilaDeLX() {
        // Verifica que a DLQ existe e aceita mensagens diretamente
        String payload = "ORC-FALHA-001";

        rabbitTemplate.convertAndSend(RabbitMQConfig.DLX_NAME, RabbitMQConfig.DEAD_LETTER_ROUTING_KEY, payload);

        Object recebido = rabbitTemplate.receiveAndConvert(RabbitMQConfig.DLQ_NAME, 5_000);

        assertNotNull(recebido, "Mensagem não chegou à DLQ dentro do timeout");
        assertEquals(payload, recebido);
    }
}
