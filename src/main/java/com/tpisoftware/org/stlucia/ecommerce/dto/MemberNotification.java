package com.tpisoftware.org.stlucia.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 會員通知資料傳輸物件（DTO）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberNotification implements Serializable {
    private Long userId;  // 用戶編號
    private String title; // 通知標題
    private String content;  // 通知內容
}