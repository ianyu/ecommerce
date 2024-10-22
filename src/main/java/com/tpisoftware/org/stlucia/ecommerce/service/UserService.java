package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 建立用戶（註冊）
    public User register(User user) {
        return userRepository.save(user);
    }

    // 根據 Email 查詢用戶
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 根據 ID 查詢用戶（新增方法）
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到用戶 ID：" + id));
    }

    // 更新用戶資訊
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到用戶 ID：" + id));
        user.setName(updatedUser.getName());
        user.setAddress(updatedUser.getAddress());
        return userRepository.save(user);
    }

}
