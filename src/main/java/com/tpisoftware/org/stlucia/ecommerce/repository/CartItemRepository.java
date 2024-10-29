package com.tpisoftware.org.stlucia.ecommerce.repository;

import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);

    List<CartItem> findByUserIdAndProductId(Long userId, Long productId);

    void deleteByIdIn(List<Long> ids);

    List<CartItem> findByIdIn(List<Long> ids);

}