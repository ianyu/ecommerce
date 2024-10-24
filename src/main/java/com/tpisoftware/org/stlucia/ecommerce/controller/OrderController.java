package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.exception.UserInvalidException;
import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Order;
import com.tpisoftware.org.stlucia.ecommerce.model.OrderItem;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.service.CartService;
import com.tpisoftware.org.stlucia.ecommerce.service.OrderService;
import com.tpisoftware.org.stlucia.ecommerce.service.UserService;
import com.tpisoftware.org.stlucia.ecommerce.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    static final String SINGLE_ATTRIBUTE_NAME = "order";
    static final String LIST_ATTRIBUTE_NAME = "orders";

    // 生成登入者的訂單（從購物車轉為訂單）
    @PostMapping
    @Transactional
    public String createByCartItem(HttpSession session, @RequestParam(name = "ids") List<Long> cartItemIds) {
        String result;

        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            result = "redirect:/auth/login";
        } else {

            Long loginUserId = jwtUtil.extractUserId(jwtToken);
            User user = userService.findById(loginUserId);
            List<CartItem> cartItems = cartService.findByIds(cartItemIds);
            Order order = transferToOrder(user, cartItems);

            order = createOrder(cartItemIds, order);

            result = "redirect:/order/" + order.getId();
        }

        return result;
    }

    // 查詢登入者的所有訂單
    @GetMapping("list")
    public String getUserOrders(HttpSession session, Model model) {
        String result;

        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            result = "redirect:/auth/login";
        } else {
            Long loginUserId = jwtUtil.extractUserId(jwtToken);
            List<Order> orders = orderService.getOrdersByUserId(loginUserId);

            model.addAttribute(LIST_ATTRIBUTE_NAME, orders);
            result = "order/list";
        }
        return result;
    }

    // 根據訂單 ID 查詢訂單詳情
    @GetMapping("{id}")
    public String getOrderDetails(HttpSession session, @PathVariable Long id, Model model) {
        String result;

        String jwtToken = (String) session.getAttribute("jwtToken");
        if (jwtToken == null) {
            result = "redirect:/auth/login";
        } else {
            Long loginUserId = jwtUtil.extractUserId(jwtToken);

            Order order = orderService.getOrderById(id);
            model.addAttribute(SINGLE_ATTRIBUTE_NAME, order);

            if (!order.getUser().getId().equals(loginUserId)) {
                throw new UserInvalidException();
            }
            result = "order/single";
        }
        return result;
    }

    // 更新訂單狀態（如：發貨、完成等）
    @PutMapping("{orderId}/status")
    public String updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/order/" + orderId;
    }

    // 取消訂單
    @DeleteMapping("{orderId}")
    public String cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders/user/" + orderService.getOrderById(orderId).getUser().getId();
    }

    private Order transferToOrder(User user, List<CartItem> cartItems) {
        // 創建新訂單並設定基本資訊
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("PENDING"); // 初始訂單狀態設置為 PENDING

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        return order;
    }

    private Order createOrder(List<Long> cartItemIds, Order order) {
        cartService.deleteByIds(cartItemIds);
        order = orderService.createOrder(order);
        return order;
    }

}