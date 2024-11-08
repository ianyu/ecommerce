package com.tpisoftware.org.stlucia.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
    private int quantity;
    private double price;
}
