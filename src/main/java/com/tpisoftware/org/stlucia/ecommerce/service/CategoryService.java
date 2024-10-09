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
    public void create(Category category) {
        categoryRepository.save(category);
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // 查詢所有商品類別
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 根據 ID 查詢商品類別
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到類別 ID：" + id));
    }

    // 更新商品類別
    public void update(Long id, Category readyToUpdate) {
        Category category = findById(id);
        category.setName(readyToUpdate.getName());
        categoryRepository.save(category);
    }

    // 刪除商品類別
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}

