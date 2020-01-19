package org.lili.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.UUID;


public class MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private final RabbitTemplate rabbitTemplate;

    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public MessageSender(final RabbitTemplate rabbitTemplate, final RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void sendMessage(Object message,String exchange, String queue) {
        rabbitAdmin.declareExchange(new FanoutExchange(exchange, true, false));
        rabbitAdmin.declareQueue(new Queue(queue, true));
        rabbitAdmin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, exchange, queue, new HashMap<>()));
        rabbitAdmin.setIgnoreDeclarationExceptions(true);
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, queue, message, correlationData);
    }
}
