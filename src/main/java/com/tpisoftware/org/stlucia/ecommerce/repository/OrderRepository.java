package com.tpisoftware.org.stlucia.ecommerce.repository;

import com.tpisoftware.org.stlucia.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId); // 根據用戶 ID 查詢訂單
}
