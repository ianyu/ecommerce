package com.tpisoftware.org.stlucia.ecommerce.mapper;

import com.tpisoftware.org.stlucia.ecommerce.dto.ProductDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.Category;
import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.Store;

public class ProductMapper {
    public static ProductDTO toDto(Product model) {
        ProductDTO result = null;
        if (model != null) {
            result = new ProductDTO();
            result.setId(model.getId());
            result.setName(model.getName());
            result.setDescription(model.getDescription());
            result.setPrice(model.getPrice());
            result.setStock(model.getStock());
            result.setStoreId(model.getStore().getId());
            if (model.getCategory() != null) {
                result.setCategoryId(model.getCategory().getId());
            }
        }
        return result;
    }

    public static Product toModel(ProductDTO dto, Store store, Category category) {
        Product model = new Product();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setDescription(dto.getDescription());
        model.setPrice(dto.getPrice());
        model.setStock(dto.getStock());
        model.setStore(store);
        model.setCategory(category);
        return model;
    }
}
