package org.lili.common.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.lili.cache.RedisClientTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lili
 * @date 2020/1/18 17:51
 * @description
 */
@ControllerAdvice(basePackages = {"org.lili.api"})
public class EventTrackingAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static Logger logger = LoggerFactory.getLogger(EventTrackingAdvice.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisClientTemplate redisClient;

    private static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean isOpen = true;
        return super.supports(returnType, converterType) && isOpen;
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        try {
            doSomething(servletRequest, bodyContainer.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doSomething(HttpServletRequest request, Object value) {
        JSONObject json = new JSONObject();
        sendMessage(json);
    }

    private void sendMessage(JSONObject json) {
        Connection connection = null;
        try {
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.expiration(new Integer(3 * 60 * 1000).toString());
            AMQP.BasicProperties properties = builder.build();
            ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
            connection = connectionFactory.createConnection();
            Channel channel = connection.createChannel(false);
            channel.exchangeDeclare("event_tracking", "topic");
            channel.queueDeclare("event_queue", true, false, false, null);
            channel.queueBind("event_queue", "event_tracking", "event_tracking");
            channel.basicPublish("event_tracking", "event_tracking", properties, JSON.toJSONBytes(json));
            channel.close();
            logger.info("counter: {}", counter.incrementAndGet());
        } catch (Exception e) {
            logger.error("send message to mq failed, exception:", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
