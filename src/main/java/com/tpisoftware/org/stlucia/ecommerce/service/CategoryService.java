package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.exception.ExceptionMessages;
import com.tpisoftware.org.stlucia.ecommerce.manager.CategorySingleton;
import com.tpisoftware.org.stlucia.ecommerce.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategorySingleton categorySingleton;

    public void create(Category category) {
        categorySingleton.save(category);
    }

    public List<Category> findAll() {
        return categorySingleton.getCategories();
    }

    public Category findById(Long id) {
        return categorySingleton.get(id).orElseThrow(() -> new IllegalArgumentException(
                String.format(ExceptionMessages.ENTITY_NOT_FOUND_WITH_ID, "category", id)));
    }

    public void update(Long id, Category readyToUpdate) {
        Category category = findById(id);
        category.setName(readyToUpdate.getName());
        categorySingleton.save(category);
    }

    public void delete(Long id) {
        categorySingleton.remove(id);
    }

}

