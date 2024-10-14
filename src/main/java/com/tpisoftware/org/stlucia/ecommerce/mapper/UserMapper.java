package com.tpisoftware.org.stlucia.ecommerce.mapper;

import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserCreateDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.User;

public class UserMapper {

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

    public static User toModel(UserCreateDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());  // 修正為 userDTO.getPassword()
        user.setAddress(userDTO.getAddress());
        return user;
    }

    public static User toModel(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());
        return user;
    }
}