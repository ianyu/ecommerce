package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.CategoryDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.StoreDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.UserDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.CategoryMapper;
import com.tpisoftware.org.stlucia.ecommerce.mapper.StoreMapper;
import com.tpisoftware.org.stlucia.ecommerce.model.Category;
import com.tpisoftware.org.stlucia.ecommerce.model.Store;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 瀏覽所有商品類別
    @GetMapping(value = "list")
    public String viewListPage(@RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                             Model model) {
        List<Category> list = categoryService.getAllCategories();

        List<CategoryDTO> result = list.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("categories", result);
        model.addAttribute("editable", editable);
        return "category/list";
    }

    // 去新增商品類別的頁面
    @GetMapping("/create")
    public String viewCreatePage(Model model) {
        model.addAttribute("category", new CategoryDTO());
        model.addAttribute("editable", true);
        return "category/single";
    }

    // 去瀏覽商品類別的頁面
    @GetMapping(value = "/{id}")
    public String toViewEditPage(@PathVariable("id") Long id,
                                  @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                                  Model model) {
        Category category = categoryService.findById(id);

        CategoryDTO categoryDTO = CategoryMapper.toDto(category);
        model.addAttribute("category", categoryDTO);
        model.addAttribute("editable", editable);
        return "category/single";
    }

    @PostMapping
    public String create(@ModelAttribute CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());

        categoryService.create(category);
        return "redirect:/category/" + category.getId();
    }

    // 更新商店資訊
    @PutMapping
    public String update(@ModelAttribute CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());

        categoryService.update(dto.getId(), category);
        return "redirect:/category/" + dto.getId();
    }

    // 刪除商品類別
    @DeleteMapping
    public String delete(@ModelAttribute CategoryDTO dto) {
        categoryService.deleteCategory(dto.getId());
        return "redirect:/category/list";
    }

}