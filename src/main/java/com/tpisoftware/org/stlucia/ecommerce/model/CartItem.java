package com.tpisoftware.org.stlucia.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 購物車商品實體類別
 */
@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // 購物車中的商品

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 所屬用戶

    private int quantity; // 商品數量
}
