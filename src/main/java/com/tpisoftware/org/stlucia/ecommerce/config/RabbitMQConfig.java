package com.tpisoftware.org.stlucia.ecommerce.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 定義會員通知相關的常量
    public static final String MEMBER_NOTIFICATION_QUEUE = "memberNotificationQueue";
    public static final String MEMBER_NOTIFICATION_EXCHANGE = "memberNotificationExchange";
    public static final String MEMBER_NOTIFICATION_ROUTING_KEY = "memberNotificationRoutingKey";

    // 定義隊列
    @Bean
    public Queue memberNotificationQueue() {
        return new Queue(MEMBER_NOTIFICATION_QUEUE);
    }

    // 定義交換機
    @Bean
    public TopicExchange memberNotificationExchange() {
        return new TopicExchange(MEMBER_NOTIFICATION_EXCHANGE);
    }

    // 定義路由鍵綁定
    @Bean
    public Binding memberNotificationBinding(Queue memberNotificationQueue, TopicExchange memberNotificationExchange) {
        return BindingBuilder.bind(memberNotificationQueue).to(memberNotificationExchange).with(MEMBER_NOTIFICATION_ROUTING_KEY);
    }

    // 訊息轉換器（使用 JSON 格式）
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 定義 RabbitTemplate 以方便訊息發送
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
