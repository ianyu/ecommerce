package com.tpisoftware.org.stlucia.ecommerce.mapper;

import com.tpisoftware.org.stlucia.ecommerce.dto.CartItemDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserCreateDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.User;

public class CartItemMapper {

    public static CartItemDTO toDto(CartItem model) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(model.getId());
        dto.setProductId(model.getProduct().getId());
        dto.setQuantity(model.getQuantity());
        return dto;
    }

    public static CartItem toModel(CartItemDTO dto, Product product, User user) {
        CartItem model = new CartItem();
        model.setId(dto.getId());
        model.setUser(user);
        model.setProduct(product);
        model.setQuantity(dto.getQuantity());
        return model;
    }

}