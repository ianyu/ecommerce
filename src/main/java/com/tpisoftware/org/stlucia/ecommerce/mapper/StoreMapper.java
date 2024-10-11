package com.tpisoftware.org.stlucia.ecommerce.mapper;

import com.tpisoftware.org.stlucia.ecommerce.dto.StoreDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.Store;
import com.tpisoftware.org.stlucia.ecommerce.model.User;

public class StoreMapper {

    public static StoreDTO toDto(Store model) {
        StoreDTO result = null;
        if (model != null) {
            result = new StoreDTO(model.getId(), model.getName(), model.getAddress(), model.getContact(), model.getOwner().getId());
        }
        return result;
    }

    public static Store toModel(StoreDTO dto, User owner) {
        Store model = new Store();
        model.setId(dto.getId());
        model.setName(dto.getName());
        model.setAddress(dto.getAddress());
        model.setContact(dto.getContact());
        model.setOwner(owner);
        return model;
    }
}