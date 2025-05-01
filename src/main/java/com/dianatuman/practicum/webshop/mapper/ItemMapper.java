package com.dianatuman.practicum.webshop.mapper;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDTO toDTO(Item itemEntity);

    Item toEntity(ItemDTO itemDTO);
}
