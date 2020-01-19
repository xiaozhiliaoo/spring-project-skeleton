package org.lili.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


public class MessageSendReturnCallBack implements RabbitTemplate.ReturnCallback {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendReturnCallBack.class);

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.debug("send failed, exchange=" + exchange + ", " + "routingKey=" + routingKey);
        logger.debug("send failed, message={}, replyCode&replyText={}", message.toString(), new Object[]{replyCode, replyText});
    }

}
