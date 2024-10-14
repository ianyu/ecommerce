package com.tpisoftware.org.stlucia.ecommerce.mapper;

import com.tpisoftware.org.stlucia.ecommerce.dto.CartItemDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserCreateDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.User;

public class CartItemMapper {

    public static UserDTO toDto(User model) {
        UserDTO result = null;
        if (model != null) {
            result = new UserDTO();
            result.setId(model.getId());
            result.setEmail(model.getEmail());
            result.setName(model.getName());
            result.setAddress(model.getAddress());
        }
        return result;
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