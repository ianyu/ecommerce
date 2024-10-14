package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.model.Product;
import com.tpisoftware.org.stlucia.ecommerce.model.Store;
import com.tpisoftware.org.stlucia.ecommerce.repository.CategoryRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.ProductRepository;
import com.tpisoftware.org.stlucia.ecommerce.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // 新增商品
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // 根據商店 ID 查詢所有商品
    public List<Product> getProductsByStoreId(Long storeId) {
        return productRepository.findByStoreId(storeId);
    }

    // 根據商品 ID 查詢商品
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到商品 ID：" + id));
    }

    // 根據類別 ID 查詢所有商品
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    // 更新商品資訊
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到商品 ID：" + id));
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        product.setCategory(updatedProduct.getCategory());
        return productRepository.save(product);
    }

    // 刪除商品
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
