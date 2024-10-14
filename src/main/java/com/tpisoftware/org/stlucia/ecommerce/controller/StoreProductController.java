package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.ProductDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.ProductMapper;
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
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/store/{storeId}/product")
public class StoreProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "list")
    public String findAllByStore(@PathVariable("storeId") Long storeId,
                                 Model model) {
        List<Product> list = productService.getProductsByStoreId(storeId);
        List<ProductDTO> result = list.stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());

        List<Category> categories = categoryService.getAllCategories();
        Map<Long, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        model.addAttribute("storeId", storeId);
        model.addAttribute("products", result);
        model.addAttribute("categoryMap", categoryMap);
        return "store/product/list";
    }

    @GetMapping("/blank")
    public String getBlankInfo(@PathVariable("storeId") Long storeId, Model model) {
        ProductDTO dto = new ProductDTO();
        dto.setStoreId(storeId);

        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("product", dto);
        model.addAttribute("categories", categories);
        model.addAttribute("editable", true);
        return "store/product/single";
    }

    @PostMapping
    public String create(@ModelAttribute ProductDTO dto) {
        Store store = storeService.findById(dto.getStoreId());
        Category category = getCategory(dto);

        Product product = ProductMapper.toModel(dto, store, category);

        productService.addProduct(product);

        return "redirect:/store/" + dto.getStoreId() + "/product/" + product.getId();
    }

    @GetMapping(value = "/{id}")
    public String browse(@PathVariable("id") Long id,
                         @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                         Model model) {
        Product product = productService.getProductById(id);
        ProductDTO dto = ProductMapper.toDto(product);

        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("product", dto);
        model.addAttribute("categories", categories);
        model.addAttribute("editable", editable);

        return "store/product/single";
    }

    @PutMapping
    public String update(@ModelAttribute ProductDTO dto) {
        Category category = getCategory(dto);

        Product product = ProductMapper.toModel(dto, null, category);

        productService.updateProduct(dto.getId(), product);

        return "redirect:/store/" + dto.getStoreId() + "/product/" + dto.getId();
    }

    @DeleteMapping
    public String delete(@ModelAttribute ProductDTO dto) {
        productService.deleteProduct(dto.getId());

        return "redirect:/store/" + dto.getStoreId() + "/product/list";
    }

    private Category getCategory(ProductDTO dto) {
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryService.findById(dto.getCategoryId());
        }
        return category;
    }

}