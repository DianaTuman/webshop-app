package com.dianatuman.practicum.webshop.mapper;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;

public interface ItemMapper {

    ItemDTO toDTO(Item itemEntity);

    Item toEntity(ItemDTO itemDTO);
}
