package org.lili.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;


public class MessageSendConfirmCallBack implements RabbitTemplate.ConfirmCallback {
    private static final Logger logger = LoggerFactory.getLogger(MessageSendConfirmCallBack.class);

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.debug("confirm correlationData={}, cause={}", correlationData, cause);
        logger.debug("confirm ack={}", ack);
        if (ack) {
            logger.debug("confirm ack message");
        } else {
            //nack
            logger.error("confirm nack message, cause={}", cause);
        }
    }
}
