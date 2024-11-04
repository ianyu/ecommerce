package com.tpisoftware.org.stlucia.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品實體類別
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 商品名稱

    private String description; // 商品描述
    private double price; // 商品價格
    private int stock; // 商品庫存

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store; // 所屬商店（對應到 Store 實體）

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // 商品類別
}
