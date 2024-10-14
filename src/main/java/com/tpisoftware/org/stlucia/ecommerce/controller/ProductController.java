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

    @GetMapping(value = "list")
    public String findAllByCategoryId(@RequestParam(name = "category", required = false) Long categoryId,
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

        List<Category> categories = categoryService.getAllCategories();
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
                         Model model) {
        Product product = productService.getProductById(id);
        ProductDTO dto = ProductMapper.toDto(product);

        List<Category> categories = categoryService.getAllCategories();

        CartItem cartItem = cartService.getCartItemsByProductId(product.getId());
        CartItemDTO cartItemDTO = new CartItemDTO();
        if (cartItem != null) {
            cartItemDTO.setId(cartItem.getId());
            cartItemDTO.setQuantity(cartItem.getQuantity());
        }
        cartItemDTO.setProductId(id);

        model.addAttribute("product", dto);
        model.addAttribute("categories", categories);
        model.addAttribute("cart", cartItemDTO);

        return "product/single";
    }

}