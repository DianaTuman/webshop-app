package com.dianatuman.practicum.webshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@ToString
public class OrderDTO implements Comparable {

    private Long id;

    private List<ItemDTO> items;

    public double getTotalSum() {
        return items.stream().map(item -> item.getPrice() * item.getCount()).reduce(0.0, Double::sum);
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

    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof OrderDTO) {
            return this.id.compareTo(((OrderDTO) o).id);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
