package com.tpisoftware.org.stlucia.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 訂單通知訊息類別
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotification {
    private Long orderId; // 訂單編號
    private Long userId;  // 用戶編號
    private String message;  // 訊息內容
}
