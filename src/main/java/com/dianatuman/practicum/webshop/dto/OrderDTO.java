package com.dianatuman.practicum.webshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class OrderDTO {

    private Long id;

    private List<ItemDTO> items;

    public double getTotalSum() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getCount()).sum();
    }
}
