package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.config.RabbitMQConfig;
import com.tpisoftware.org.stlucia.ecommerce.dto.MemberNotification;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducerService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    // 發送通知到 RabbitMQ 隊列
    public void sendNotification(Long userId, String title, String content) {
        MemberNotification notification = new MemberNotification(userId, title, content);
        amqpTemplate.convertAndSend(RabbitMQConfig.MEMBER_NOTIFICATION_EXCHANGE, RabbitMQConfig.MEMBER_NOTIFICATION_ROUTING_KEY, notification);
    }
}
