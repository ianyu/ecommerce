package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.CartItemDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.CartItemMapper;
import com.tpisoftware.org.stlucia.ecommerce.mapper.UserMapper;
import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.service.CartService;
import com.tpisoftware.org.stlucia.ecommerce.service.CategoryService;
import com.tpisoftware.org.stlucia.ecommerce.service.ProductService;
import com.tpisoftware.org.stlucia.ecommerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartItemController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public String create(@ModelAttribute CartItemDTO dto, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        cartService.addToCart(loginUser.getId(), dto.getProductId(), dto.getQuantity());

        return "redirect:/product/" + dto.getProductId();
    }

    @PutMapping
    public String update(@ModelAttribute CartItemDTO dto, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        User user = UserMapper.toModel(loginUser);

        Product product = productService.getProductById(dto.getProductId());

        CartItem cart = CartItemMapper.toModel(dto, product, user);
        cartService.update(dto.getId(), cart);

        return "redirect:/product/" + dto.getProductId();
    }


    // 查詢購物車中的商品
    @GetMapping
    public String getCart(Model model, HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        List<CartItem> cartItems = cartService.getCartItems(loginUser.getId());
        model.addAttribute("cartItems", cartItems);
        return "cart/list";
    }

    @DeleteMapping("{id}")
    public String removeCartItem(@PathVariable("id") Long id) {
        cartService.delete(id);
        return "redirect:/cart";
    }

    @DeleteMapping
    public String clearCart(HttpSession session) {
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        cartService.clearCart(loginUser.getId());
        return "redirect:/cart";
    }

}