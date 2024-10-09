package com.tpisoftware.org.stlucia.ecommerce.mapper;

import com.tpisoftware.org.stlucia.ecommerce.dto.CategoryDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.Category;

public class CategoryMapper {
    public static CategoryDTO toDto(Category model) {
        CategoryDTO result = null;
        if (model != null) {
            result = new CategoryDTO(model.getId(), model.getName());
        }
        return result;
    }
}
