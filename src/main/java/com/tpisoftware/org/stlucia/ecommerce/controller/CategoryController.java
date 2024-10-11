package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.CategoryDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.CategoryMapper;
import com.tpisoftware.org.stlucia.ecommerce.model.Category;
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

    @GetMapping(value = "list")
    public String findAll(Model model) {
        List<Category> list = categoryService.getAllCategories();

        List<CategoryDTO> result = list.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("categories", result);
        return "category/list";
    }

    @GetMapping("/create")
    public String getBlankInfo(Model model) {
        model.addAttribute("category", new CategoryDTO());
        model.addAttribute("editable", true);
        return "category/single";
    }

    @PostMapping
    public String create(@ModelAttribute CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());

        categoryService.create(category);
        return "redirect:/category/" + category.getId();
    }

    @GetMapping(value = "/{id}")
    public String browse(@PathVariable("id") Long id,
                         @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                         Model model) {
        Category category = categoryService.findById(id);

        CategoryDTO dto = CategoryMapper.toDto(category);
        model.addAttribute("category", dto);
        model.addAttribute("editable", editable);
        return "category/single";
    }

    @PutMapping
    public String update(@ModelAttribute CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());

        categoryService.update(dto.getId(), category);
        return "redirect:/category/" + dto.getId();
    }

    @DeleteMapping
    public String delete(@ModelAttribute CategoryDTO dto) {
        categoryService.deleteCategory(dto.getId());
        return "redirect:/category/list";
    }

}