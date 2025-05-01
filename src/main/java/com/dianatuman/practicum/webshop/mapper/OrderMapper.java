package com.dianatuman.practicum.webshop.mapper;

import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.entity.Order;

public interface OrderMapper {

    OrderDTO toDTO(Order orderEntity);

    Order toEntity(OrderDTO orderDTO);
}
