package com.tpisoftware.org.stlucia.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 會員資料傳輸物件（DTO）
 */
@Data  // 這個注解會自動生成 getter, setter, toString, equals, 和 hashCode 方法
@NoArgsConstructor  // 自動生成無參數建構子
@AllArgsConstructor // 自動生成有參數建構子
public class UserDTO {
    private Long id;
    private String email;
    private String password; // 確保包含 password 欄位
    private String name;
    private String address;
}
