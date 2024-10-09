package com.tpisoftware.org.stlucia.ecommerce.service;

import com.tpisoftware.org.stlucia.ecommerce.model.Store;
import com.tpisoftware.org.stlucia.ecommerce.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;

    // 建立新商店
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    // 根據店主 ID 查詢所有商店
    public List<Store> getStoresByOwnerId(Long ownerId) {
        return storeRepository.findByOwnerId(ownerId);
    }

    // 根據商店 ID 查詢商店（新增方法）
    public Store findById(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到商店 ID：" + id));
    }

    // 更新商店資訊
    public Store updateStore(Long id, Store updatedStore) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("找不到商店 ID：" + id));
        store.setName(updatedStore.getName());
        store.setAddress(updatedStore.getAddress());
        store.setContact(updatedStore.getContact());
        return storeRepository.save(store);
    }

}