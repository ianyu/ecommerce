package com.tpisoftware.org.stlucia.ecommerce.mapper;

import com.tpisoftware.org.stlucia.ecommerce.dto.StoreDTO;
import com.tpisoftware.org.stlucia.ecommerce.model.Store;

public class StoreMapper {

    public static StoreDTO toDto(Store model) {
        StoreDTO result = null;
        if (model != null) {
            result = new StoreDTO(model.getId(), model.getName(), model.getAddress(), model.getContact(), model.getOwner().getId());
        }
        return result;
    }

}