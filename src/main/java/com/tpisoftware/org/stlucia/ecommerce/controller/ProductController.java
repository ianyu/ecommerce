package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.CartItemDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.ProductDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.ProductMapper;
import com.tpisoftware.org.stlucia.ecommerce.model.CartItem;
import com.tpisoftware.org.stlucia.ecommerce.model.Category;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.service.CartService;
import com.tpisoftware.org.stlucia.ecommerce.service.CategoryService;
import com.tpisoftware.org.stlucia.ecommerce.service.ProductService;
import com.tpisoftware.org.stlucia.ecommerce.util.JwtUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(value = "list")
    public String findAllByCategoryId(@RequestParam(name = "category", required = false) Long categoryId,
                                      HttpSession session,
                                      Model model) {
        List<Product> list;
        if (categoryId == null) {
            list = productService.getProducts();
        } else {
            list = productService.getProductsByCategoryId(categoryId);
        }

        List<ProductDTO> result = list.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());

        List<Category> categories = categoryService.findAll();
        Map<Long, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        model.addAttribute("categoryId", categoryId);
        model.addAttribute("products", result);
        model.addAttribute("categoryMap", categoryMap);
        return "product/list";
    }

    @GetMapping(value = "/{id}")
    public String browse(@PathVariable("id") Long id,
                         @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                         HttpSession session,
                         Model model) {
        String result = "product/single";

        Product product = productService.getProductById(id);

        if (product == null) {
            result = "redirect:/product/list";
        } else {
            ProductDTO dto = ProductMapper.toDto(product);
            List<Category> categories = categoryService.findAll();

            String jwtToken = (String) session.getAttribute("jwtToken");
            CartItemDTO cartItemDTO;
            if (jwtToken == null) {
                cartItemDTO = getSessionCartItemDTO(session, product);
            } else {
                Long loginUserId = jwtUtil.extractUserId(jwtToken);
                cartItemDTO = getUserCartItemDTO(loginUserId, product.getId());
            }

            model.addAttribute("product", dto);
            model.addAttribute("cart", cartItemDTO);
        }

        return result;
    }

    private CartItemDTO getSessionCartItemDTO(HttpSession session, Product product) {
        CartItemDTO cartItemDTO;
        List<CartItemDTO> cartItemDTOs =
                CartItemController.getSessionCartItemDTOs(session.getAttribute(CartItemController.ATTRIBUTE_NAME));
        cartItemDTO = cartItemDTOs.stream()
                .filter(o -> o.getProductId().equals(product.getId()))
                .findFirst()
                .orElse(new CartItemDTO(null, product.getId(), 0));
        return cartItemDTO;
    }

    private CartItemDTO getUserCartItemDTO(Long userId, Long productId) {
        CartItemDTO cartItemDTO;
        CartItem cartItem = cartService.findByUserIdAndProductId(userId, productId);

        cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(productId);
        if (cartItem != null) {
            cartItemDTO.setId(cartItem.getId());
            cartItemDTO.setQuantity(cartItem.getQuantity());
        }
        return cartItemDTO;
    }

}