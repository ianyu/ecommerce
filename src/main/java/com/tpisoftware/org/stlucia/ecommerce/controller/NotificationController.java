package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.model.Notification;
import com.tpisoftware.org.stlucia.ecommerce.service.NotificationService;
import com.tpisoftware.org.stlucia.ecommerce.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/list")
    public String findAllByLoginUser(HttpSession session, Model model) {
        String result;
        String jwtToken = (String) session.getAttribute("jwtToken");

        if (jwtToken == null) {
            result = "redirect:/auth/login";
        } else {
            Long loginUserId = jwtUtil.extractUserId(jwtToken);
            List<Notification> notifications = notificationService.getNotificationsByUserId(loginUserId);
            model.addAttribute("notifications", notifications);

            result = "notification/list";
        }
        return result;
    }

    @GetMapping("{id}")
    public String getNotificationDetails(@PathVariable Long id, Model model) {
        Notification notification = notificationService.getNotificationById(id);
        model.addAttribute("notification", notification);
        return "notification/single";
    }

    @PutMapping("{id}")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/notification/list";
    }
}