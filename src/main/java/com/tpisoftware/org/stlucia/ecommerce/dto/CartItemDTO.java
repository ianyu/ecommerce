package com.tpisoftware.org.stlucia.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 購物車資料傳輸物件（DTO）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long productId; // 商品 ID
    private int quantity; // 商品數量
}
