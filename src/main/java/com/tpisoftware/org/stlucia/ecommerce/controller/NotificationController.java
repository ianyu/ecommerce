package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.model.Notification;
import com.tpisoftware.org.stlucia.ecommerce.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // 查詢會員的所有通知
    @GetMapping("/user/{userId}")
    public String getUserNotifications(@PathVariable Long userId, Model model) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        model.addAttribute("notifications", notifications);
        return "notification-list";
    }

    // 查詢通知詳情
    @GetMapping("/{notificationId}")
    public String getNotificationDetails(@PathVariable Long notificationId, Model model) {
        Notification notification = notificationService.getNotificationById(notificationId);
        model.addAttribute("notification", notification);
        return "notification-details";
    }

    // 標記通知為已讀
    @PostMapping("/{notificationId}/read")
    public String markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        Notification notification = notificationService.getNotificationById(notificationId);
        return "redirect:/notifications/user/" + notification.getUser().getId();
    }
}
