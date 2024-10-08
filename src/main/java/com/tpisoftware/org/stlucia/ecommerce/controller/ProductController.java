package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.ProductDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.Category;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.Store;
import com.tpisoftware.org.stlucia.ecommerce.service.CategoryService;
import com.tpisoftware.org.stlucia.ecommerce.service.ProductService;
import com.tpisoftware.org.stlucia.ecommerce.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CategoryService categoryService;

    // 顯示新增商品的表單
    @GetMapping("/add/{storeId}")
    public String showAddProductForm(@PathVariable Long storeId, Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("storeId", storeId);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-add";
    }

    // 新增商品
    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        Store store = storeService.getStoreById(productDTO.getStoreId()); // 找到對應商店
        Category category = categoryService.getCategoryById(productDTO.getCategoryId()); // 找到對應類別

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setStore(store);
        product.setCategory(category);

        productService.addProduct(product);
        return "redirect:/products/store/" + productDTO.getStoreId();
    }

    // 根據商店 ID 查詢所有商品
    @GetMapping("/store/{storeId}")
    public String getProductsByStore(@PathVariable Long storeId, Model model) {
        List<Product> products = productService.getProductsByStoreId(storeId);
        model.addAttribute("products", products);
        return "product-list";
    }

    // 顯示編輯商品資訊的表單
    @GetMapping("/{id}/edit")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-edit";
    }

    // 更新商品資訊
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductDTO productDTO) {
        Category category = categoryService.getCategoryById(productDTO.getCategoryId());

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCategory(category);

        productService.updateProduct(id, product);
        return "redirect:/products/store/" + productDTO.getStoreId();
    }

    // 刪除商品
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products/store/" + productService.getProductById(id).getStore().getId();
    }
}
