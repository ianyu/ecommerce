package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.CartItemDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.ProductDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.CartItemMapper;
import com.tpisoftware.org.stlucia.ecommerce.mapper.ProductMapper;
import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.service.CartService;
import com.tpisoftware.org.stlucia.ecommerce.service.ProductService;
import com.tpisoftware.org.stlucia.ecommerce.service.UserService;
import com.tpisoftware.org.stlucia.ecommerce.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartItemController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    static final String ATTRIBUTE_NAME = "cartItems";

    @PostMapping
    public String create(@ModelAttribute CartItemDTO dto, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");

        if (jwtToken == null) {
            List<CartItemDTO> sessionCartItemDTOs = getSessionCartItemDTOs(session.getAttribute(ATTRIBUTE_NAME));

            long maxId = sessionCartItemDTOs.stream()
                    .mapToLong(CartItemDTO::getId)
                    .max()
                    .orElse(0L);

            dto.setId(++maxId);

            List<CartItemDTO> updatedCartItems = new ArrayList<>(sessionCartItemDTOs.stream()
                    .map(item -> item.getProductId().equals(dto.getProductId()) ? dto : item)
                    .filter(item -> item.getQuantity() > 0)
                    .toList());

            // 計算匹配的數量
            long count = updatedCartItems.stream()
                    .filter(item -> item.getProductId().equals(dto.getProductId()))
                    .count();

            if (count == 0 && dto.getQuantity() > 0) {
                updatedCartItems.add(dto);
            }

            session.setAttribute(ATTRIBUTE_NAME, updatedCartItems);
        } else {
            Long loginUserId = getLoginUserId(jwtToken);
            if (dto.getQuantity() > 0) {
                cartService.addToCart(loginUserId, dto.getProductId(), dto.getQuantity());
            }
        }

        return "redirect:/product/" + dto.getProductId();
    }

    @PutMapping
    public String update(@ModelAttribute CartItemDTO dto, HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");

        if (jwtToken == null) {
            List<CartItemDTO> sessionCartItemDTOs = getSessionCartItemDTOs(session.getAttribute(ATTRIBUTE_NAME));

            long maxId = sessionCartItemDTOs.stream()
                    .mapToLong(CartItemDTO::getId)
                    .max()
                    .orElse(0L);

            dto.setId(++maxId);

            List<CartItemDTO> updatedCartItems = new ArrayList<>(sessionCartItemDTOs.stream()
                    .map(item -> item.getProductId().equals(dto.getProductId()) ? dto : item)
                    .filter(item -> item.getQuantity() > 0)
                    .toList()); // 收集到新的列表中

            // 計算匹配的數量
            long count = updatedCartItems.stream()
                    .filter(item -> item.getProductId().equals(dto.getProductId()))
                    .count();

            if (count == 0 && dto.getQuantity() > 0) {
                updatedCartItems.add(dto);
            }

            session.setAttribute(ATTRIBUTE_NAME, updatedCartItems);
        } else {
            Long loginUserId = getLoginUserId(jwtToken);
            if (dto.getQuantity() > 0) {
                User user = userService.findById(loginUserId);
                Product product = productService.getProductById(dto.getProductId());
                CartItem cart = CartItemMapper.toModel(dto, product, user);
                cartService.update(dto.getId(), cart);
            } else {
                cartService.delete(dto.getId());
            }
        }

        return "redirect:/product/" + dto.getProductId();
    }


    // 查詢購物車中的商品
    @GetMapping
    public String getCart(HttpSession session, Model model) {
        String jwtToken = (String) session.getAttribute("jwtToken");

        List<CartItemDTO> cartItemDTOs;
        if (jwtToken == null) {
            cartItemDTOs = getSessionCartItemDTOs(session.getAttribute(ATTRIBUTE_NAME));
        } else {
            Long loginUserId = getLoginUserId(jwtToken);
            List<CartItem> cartItems = cartService.getCartItems(loginUserId);
            cartItemDTOs = cartItems.stream()
                    .map(CartItemMapper::toDto)
                    .collect(Collectors.toList());
        }

        Map<Long, ProductDTO> productMap;
        if (cartItemDTOs.size() > 0) {
            List<Long> productIds = cartItemDTOs.stream().map(CartItemDTO::getProductId).toList();
            List<Product> products = productService.findByIdIn(productIds);
            productMap = products.stream().collect(Collectors.toMap(Product::getId, ProductMapper::toDto));

            session.setAttribute(ATTRIBUTE_NAME, cartItemDTOs);
            model.addAttribute("productMap", productMap);
            System.out.println("cartItemDTOs:" + cartItemDTOs);
            System.out.println("productMap:" + productMap);
        }

        return "cart/list";
    }

    @DeleteMapping("{id}")
    public String removeCartItem(HttpSession session, @PathVariable("id") Long id) {
        String jwtToken = (String) session.getAttribute("jwtToken");

        if (jwtToken == null) {
            List<CartItemDTO> cartItemDTOs = getSessionCartItemDTOs(session.getAttribute(ATTRIBUTE_NAME));
            List<CartItemDTO> newDTOs = cartItemDTOs.stream()
                    .filter(cartItem -> !cartItem.getId().equals(id)).toList();
            session.setAttribute(ATTRIBUTE_NAME, newDTOs);
        } else {
            cartService.delete(id);
        }
        return "redirect:/cart";
    }

    @DeleteMapping
    public String clearCart(HttpSession session) {
        String jwtToken = (String) session.getAttribute("jwtToken");

        if (jwtToken == null) {
            session.removeAttribute(ATTRIBUTE_NAME);
        } else {
            Long loginUserId = getLoginUserId(jwtToken);
            cartService.clearCart(loginUserId);

        }
        return "redirect:/cart";
    }

    private Long getLoginUserId(String jwtToken) {
        return jwtUtil.extractUserId(jwtToken);
    }

    @SuppressWarnings("unchecked")
    static List<CartItemDTO> getSessionCartItemDTOs(Object obj) {
        List<CartItemDTO> cartItemDTOs;
        if (obj == null) {
            cartItemDTOs = new ArrayList<>();
        } else {
            cartItemDTOs = (List<CartItemDTO>) obj;
        }
        return cartItemDTOs;
    }

}