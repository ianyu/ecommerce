package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.model.Category;
import com.tpisoftware.org.stlucia.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // 新增商品類別
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // 查詢所有商品類別
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 根據類別 ID 查詢商品類別
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到類別 ID：" + id));
    }

    // 更新商品類別
    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到類別 ID：" + id));
        category.setName(updatedCategory.getName());
        return categoryRepository.save(category);
    }

    // 刪除商品類別
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}

