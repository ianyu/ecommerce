package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.CartItemDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    // 查詢購物車中的商品
    @GetMapping("/{userId}")
    public String getCart(@PathVariable Long userId, Model model) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    // 新增商品到購物車
    @PostMapping("/add")
    public String addToCart(@ModelAttribute CartItemDTO cartItemDTO) {
        cartService.addToCart(cartItemDTO.getUserId(), cartItemDTO.getProductId(), cartItemDTO.getQuantity());
        return "redirect:/cart/" + cartItemDTO.getUserId();
    }

    // 移除購物車中的某個商品
    @PostMapping("/{userId}/remove/{productId}")
    public String removeItem(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeItem(userId, productId);
        return "redirect:/cart/" + userId;
    }

    // 清空購物車
    @PostMapping("/{userId}/clear")
    public String clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return "redirect:/cart/" + userId;
    }
}
