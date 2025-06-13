package com.dianatuman.practicum.webshop.mapper;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "count", ignore = true)
    ItemDTO toDTO(Item itemEntity);

    ItemDTO orderItemToDTO(OrderItem itemEntity);

    Item toEntity(ItemDTO itemDTO);

}
