package com.tpisoftware.org.stlucia.ecommerce.controller;


import com.tpisoftware.org.stlucia.ecommerce.dto.StoreDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.Store;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.service.StoreService;
import com.tpisoftware.org.stlucia.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stores")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    // 顯示新增商店的表單
    @GetMapping("/create")
    public String showCreateStoreForm(Model model) {
        model.addAttribute("store", new StoreDTO());
        return "store-create";
    }

    // 新增商店
    @PostMapping("/create")
    public String createStore(@ModelAttribute StoreDTO storeDTO) {
        // 根據 storeDTO 的 ownerId 找到對應的 User 實體
        User owner = userService.findById(storeDTO.getOwnerId());

        Store store = new Store();
        store.setName(storeDTO.getName());
        store.setAddress(storeDTO.getAddress());
        store.setContact(storeDTO.getContact());
        store.setOwner(owner);

        storeService.createStore(store);
        return "redirect:/stores/owner/" + storeDTO.getOwnerId();
    }

    // 根據店主 ID 查詢其所有商店
    @GetMapping("/owner/{ownerId}")
    public String getStoresByOwner(@PathVariable Long ownerId, Model model) {
        List<Store> stores = storeService.getStoresByOwnerId(ownerId);
        model.addAttribute("stores", stores);
        return "store-list";
    }

    // 顯示編輯商店資訊的表單
    @GetMapping("/{id}/edit")
    public String showEditStoreForm(@PathVariable Long id, Model model) {
        Store store = storeService.getStoreById(id);
        model.addAttribute("store", store);
        return "store-edit";
    }

    // 更新商店資訊
    @PostMapping("/{id}/edit")
    public String updateStore(@PathVariable Long id, @ModelAttribute StoreDTO storeDTO) {
        Store store = new Store();
        store.setName(storeDTO.getName());
        store.setAddress(storeDTO.getAddress());
        store.setContact(storeDTO.getContact());

        storeService.updateStore(id, store);
        return "redirect:/stores/owner/" + storeDTO.getOwnerId();
    }
}
