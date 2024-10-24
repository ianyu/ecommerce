package com.tpisoftware.org.stlucia.ecommerce.repository;

import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId); // 根據用戶 ID 查詢購物車中的所有商品

    List<CartItem> findByProductId(Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId); // 根據用戶 ID 和商品 ID 刪除購物車中的商品

    void deleteByIdIn(List<Long> ids);

    List<CartItem> findByIdIn(List<Long> ids);
}
