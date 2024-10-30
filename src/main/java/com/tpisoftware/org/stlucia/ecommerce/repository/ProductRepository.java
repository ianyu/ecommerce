package com.tpisoftware.org.stlucia.ecommerce.repository;

import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStoreId(Long storeId); // 根據商店 ID 查詢商品
    List<Product> findByCategoryId(Long categoryId); // 根據類別 ID 查詢商品

    List<Product> findByIdIn(List<Long> ids);
}

