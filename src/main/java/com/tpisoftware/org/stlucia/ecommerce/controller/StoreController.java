package com.tpisoftware.org.stlucia.ecommerce.controller;

import com.tpisoftware.org.stlucia.ecommerce.dto.StoreDTO;
import com.tpisoftware.org.stlucia.ecommerce.dto.user.UserDTO;
import com.tpisoftware.org.stlucia.ecommerce.mapper.StoreMapper;
import com.tpisoftware.org.stlucia.ecommerce.mapper.UserMapper;
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

    @GetMapping(value = "list")
    public String findAllByUser(@RequestParam("owner") Long ownerId,
                                @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                                Model model) {
        User user = userService.findById(ownerId);
        UserDTO ownerDTO = UserMapper.toDto(user);

        List<Store> list = storeService.getStoresByOwnerId(ownerId);
        List<StoreDTO> result = list.stream()
                .map(StoreMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("owner", ownerDTO);
        model.addAttribute("stores", result);
        model.addAttribute("editable", editable);
        return "store/list";
    }

    @GetMapping("/create")
    public String getBlankInfo(@RequestParam("owner") Long ownerId, Model model) {
        StoreDTO dto = new StoreDTO();
        dto.setOwnerId(ownerId);

        model.addAttribute("store", dto);
        model.addAttribute("editable", true);
        return "store/single";
    }

    @PostMapping
    public String create(@ModelAttribute StoreDTO dto) {
        User owner = userService.findById(dto.getOwnerId());
        Store store = StoreMapper.toModel(dto, owner);

        storeService.createStore(store);

        return "redirect:/store/" + store.getId();
    }

    @GetMapping(value = "/{id}")
    public String browse(@PathVariable("id") Long id,
                         @RequestParam(name = "editable", defaultValue = "false") Boolean editable,
                         Model model) {
        Store store = storeService.findById(id);
        StoreDTO dto = StoreMapper.toDto(store);

        model.addAttribute("store", dto);
        model.addAttribute("editable", editable);

        return "store/single";
    }

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