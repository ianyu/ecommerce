package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.model.Notification;
import com.tpisoftware.org.stlucia.ecommerce.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // 根據會員 ID 查詢所有通知
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByDateDesc(userId);
    }

    // 根據通知 ID 查詢通知
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到通知 ID：" + id));
    }

    // 標記通知為已讀
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new IllegalArgumentException("找不到通知 ID：" + notificationId));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
