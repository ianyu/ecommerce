package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.exception.ExceptionMessages;
import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.repository.CartItemRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.ProductRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(
                String.format(ExceptionMessages.ENTITY_NOT_FOUND_WITH_ID, "user", userId)));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException(
                String.format(ExceptionMessages.ENTITY_NOT_FOUND_WITH_ID, "product", productId)));

        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    public CartItem update(Long id, CartItem cartItem) {
        cartItem.setId(id);
        return cartItemRepository.save(cartItem);
    }

    // 根據用戶 ID 查詢購物車中的所有商品
    public List<CartItem> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public CartItem getCartItemsByProductId(Long productId) {
        CartItem cartItem = null;
        List<CartItem> list = cartItemRepository.findByProductId(productId);
        if (list.size() > 0) {
            cartItem = list.get(0);
        }
        return cartItem;
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

    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("request list cannot be empty.");
        }
        cartItemRepository.deleteByIdIn(ids);
    }

    public List<CartItem> findByIds(List<Long> ids) {
        return cartItemRepository.findByIdIn(ids);
    }
}
