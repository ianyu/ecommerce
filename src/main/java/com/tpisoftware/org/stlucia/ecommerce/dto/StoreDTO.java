package com.tpisoftware.org.stlucia.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商店資料傳輸物件（DTO）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String address;
    private String contact;
    private Long ownerId; // 所屬會員（店主）ID
}
