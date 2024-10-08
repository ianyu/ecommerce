package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.repository.CartItemRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.ProductRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // 新增商品到購物車
    public CartItem addToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("找不到用戶 ID：" + userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("找不到商品 ID：" + productId));

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    // 根據用戶 ID 查詢購物車中的所有商品
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    // 根據用戶 ID 和商品 ID 移除購物車中的商品
    public void removeItem(Long userId, Long productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
    }

    // 清空購物車
    public void clearCart(Long userId) {
        List<CartItem> items = getCartItems(userId);
        cartItemRepository.deleteAll(items);
    }
}
