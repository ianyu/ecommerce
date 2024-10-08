package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.config.RabbitMQConfig;
import com.tpisoftware.org.stlucia.ecommerce.dto.MemberNotification;
import com.tpisoftware.org.stlucia.ecommerce.model.Notification;
import com.tpisoftware.org.stlucia.ecommerce.repository.NotificationRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NotificationConsumerService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    // 從會員通知隊列中接收訊息並儲存到資料庫中
    @RabbitListener(queues = RabbitMQConfig.MEMBER_NOTIFICATION_QUEUE)
    public void handleNotification(MemberNotification memberNotification) {
        System.out.println("接收到通知：用戶編號：" + memberNotification.getUserId() + "，標題：" + memberNotification.getTitle());

        Notification notification = new Notification();
        notification.setUser(userRepository.findById(memberNotification.getUserId()).orElseThrow(() -> new IllegalArgumentException("找不到用戶 ID：" + memberNotification.getUserId())));
        notification.setTitle(memberNotification.getTitle());
        notification.setContent(memberNotification.getContent());
        notification.setDate(new Date());
        notification.setRead(false);

        // 儲存通知到資料庫中
        notificationRepository.save(notification);
    }
}
