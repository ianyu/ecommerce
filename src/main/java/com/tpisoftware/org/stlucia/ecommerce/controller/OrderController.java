package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.OrderDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.Order;
import com.tpisoftware.org.stlucia.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // 生成訂單（從購物車轉為訂單）
    @GetMapping("/checkout/{userId}")
    public String checkout(@PathVariable Long userId) {
        orderService.createOrder(userId);
        return "redirect:/orders/user/" + userId;
    }

    // 根據用戶 ID 查詢其所有訂單
    @GetMapping("/user/{userId}")
    public String getUserOrders(@PathVariable Long userId, Model model) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "order-list";
    }

    // 根據訂單 ID 查詢訂單詳情
    @GetMapping("/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order-details";
    }

    // 更新訂單狀態（如：發貨、完成等）
    @PostMapping("/{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/orders/" + orderId;
    }

    // 取消訂單
    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders/user/" + orderService.getOrderById(orderId).getUser().getId();
    }
}
