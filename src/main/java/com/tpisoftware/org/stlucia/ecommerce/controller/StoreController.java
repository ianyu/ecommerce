package com.tpisoftware.org.stlucia.ecommerce.controller;


import com.tpisoftware.org.stlucia.ecommerce.dto.StoreDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.UserDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.StoreMapper;
import com.tpisoftware.org.stlucia.ecommerce.model.Store;
import com.tpisoftware.org.stlucia.ecommerce.model.User;
import com.tpisoftware.org.stlucia.ecommerce.service.StoreService;
import com.tpisoftware.org.stlucia.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    // 瀏覽店主的所有商店
    @GetMapping(value = "list")
    public String viewListPage(@RequestParam("owner") Long ownerId,
                             @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                             Model model) {
        User user = userService.findById(ownerId);
        UserDTO ownerDTO = new UserDTO(user.getId(), user.getEmail(), user.getPassword(),
                user.getName(), user.getAddress());

        List<Store> list = storeService.getStoresByOwnerId(ownerId);

        List<StoreDTO> result = list.stream()
                .map(StoreMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("owner", ownerDTO);
        model.addAttribute("stores", result);
        model.addAttribute("editable", editable);
        return "store/list";
    }

    // 去新增商店的頁面
    @GetMapping("/create")
    public String viewCreatePage(@RequestParam("owner") Long ownerId, Model model) {
        StoreDTO store = new StoreDTO();
        store.setOwnerId(ownerId);
        model.addAttribute("store", store);
        model.addAttribute("editable", true);
        return "store/single";
    }

    // 去瀏覽商店的頁面
    @GetMapping(value = "/{id}")
    public String viewEditPage(@PathVariable("id") Long id,
                                  @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                                  Model model) {
        Store store = storeService.findById(id);

        StoreDTO storeDTO = StoreMapper.toDto(store);
        model.addAttribute("store", storeDTO);
        model.addAttribute("editable", editable);
        return "store/single";
    }

    @PostMapping
    public String create(@ModelAttribute StoreDTO dto) {
        User owner = userService.findById(dto.getOwnerId());
        Store store = new Store();
        store.setName(dto.getName());
        store.setAddress(dto.getAddress());
        store.setContact(dto.getContact());
        store.setOwner(owner);

        storeService.createStore(store);
        return "redirect:/store/" + store.getId();
    }

    // 更新商店資訊
    @PutMapping
    public String update(@ModelAttribute StoreDTO dto) {
        Store store = new Store();
        store.setName(dto.getName());
        store.setAddress(dto.getAddress());
        store.setContact(dto.getContact());

        storeService.updateStore(dto.getId(), store);
        return "redirect:/store/" + dto.getId();
    }

}
