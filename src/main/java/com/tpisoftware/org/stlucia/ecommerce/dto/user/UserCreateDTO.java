package com.tpisoftware.org.stlucia.ecommerce.dto.user;

import lombok.*;

/**
 * 會員資料傳輸物件（DTO）
 */
@Getter
@Setter
@NoArgsConstructor  // 自動生成無參數建構子
@AllArgsConstructor // 自動生成有參數建構子
public class UserCreateDTO extends UserDTO {
    private String password; // 確保包含 password 欄位
}
