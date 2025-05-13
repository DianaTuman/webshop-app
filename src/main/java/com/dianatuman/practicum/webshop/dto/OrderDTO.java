package com.dianatuman.practicum.webshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@ToString
public class OrderDTO {

    private Long id;

    private List<ItemDTO> items;

    public double getTotalSum() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getCount()).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(id, orderDTO.id) && Objects.equals(items, orderDTO.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, items);
    }
}
