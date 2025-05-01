package com.dianatuman.practicum.webshop.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {

    private Long id;

    private List<ItemDTO> items;

    public double getTotalSum() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getCount()).sum();
    }
}
