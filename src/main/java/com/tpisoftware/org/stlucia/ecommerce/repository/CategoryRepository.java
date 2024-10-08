package com.tpisoftware.org.stlucia.ecommerce.repository;

import com.tpisoftware.org.stlucia.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

