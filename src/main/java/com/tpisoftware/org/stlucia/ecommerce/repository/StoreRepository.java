package com.tpisoftware.org.stlucia.ecommerce.repository;


import com.tpisoftware.org.stlucia.ecommerce.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByOwnerId(Long ownerId); // 根據店主 ID 查詢商店
}
