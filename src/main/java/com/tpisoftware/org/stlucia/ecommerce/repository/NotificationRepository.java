package com.tpisoftware.org.stlucia.ecommerce.repository;

import com.tpisoftware.org.stlucia.ecommerce.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 根據會員 ID 查詢該會員的所有通知
    List<Notification> findByUserIdOrderByDateDesc(Long userId);
}
