package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Order;
import com.tpisoftware.org.stlucia.ecommerce.model.OrderItem;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.repository.CartItemRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.OrderRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationProducerService notificationProducerService; // 使用 RabbitMQ 的 Producer

    /**
     * 生成訂單，並將購物車中的商品轉為訂單
     *
     * @param userId 會員 ID
     * @return 新生成的訂單
     */
    @Transactional
    public Order createOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("找不到用戶 ID：" + userId));
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        // 創建新訂單並設定基本資訊
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("PENDING"); // 初始訂單狀態設置為 PENDING

        // 將購物車中的商品轉為訂單項目
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

        // 清空購物車
        cartItemRepository.deleteAll(cartItems);

        // 儲存新訂單
        Order savedOrder = orderRepository.save(order);

        // 發送訂單生成通知（使用 RabbitMQ）
        notificationProducerService.sendNotification(userId, "訂單已生成", "您的訂單已成功建立，訂單編號：" + savedOrder.getId());

        return savedOrder;
    }

    /**
     * 根據會員 ID 查詢其所有訂單
     *
     * @param userId 會員 ID
     * @return 該會員的所有訂單
     */
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /**
     * 根據訂單 ID 查詢訂單
     *
     * @param id 訂單 ID
     * @return 查詢到的訂單
     */
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到訂單 ID：" + id));
    }

    /**
     * 更新訂單狀態（如：發貨、完成等）
     *
     * @param orderId 訂單 ID
     * @param status  新的訂單狀態
     */
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("找不到訂單 ID：" + orderId));
        order.setStatus(status);
        orderRepository.save(order);

        // 發送訂單狀態變更通知（使用 RabbitMQ）
        notificationProducerService.sendNotification(order.getUser().getId(), "訂單狀態變更",
                "您的訂單狀態已變更為：" + status);
    }

    /**
     * 取消訂單
     *
     * @param orderId 訂單 ID
     */
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("找不到訂單 ID：" + orderId));
        order.setStatus("CANCELLED");
        orderRepository.save(order);

        // 發送訂單取消通知（使用 RabbitMQ）
        notificationProducerService.sendNotification(order.getUser().getId(), "訂單取消", "您的訂單已被取消。");
    }
}
