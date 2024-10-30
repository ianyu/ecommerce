package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.exception.ExceptionMessages;
import com.tpisoftware.org.stlucia.ecommerce.model.Notification;
import com.tpisoftware.org.stlucia.ecommerce.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByDateDesc(userId);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                String.format(ExceptionMessages.ENTITY_NOT_FOUND_WITH_ID, "notification", id)));
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                String.format(ExceptionMessages.ENTITY_NOT_FOUND_WITH_ID, "notification", id)));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

}