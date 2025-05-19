package com.dianatuman.practicum.webshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Data
@AllArgsConstructor
@ToString
public class OrderDTO {

    private Long id;

    private Flux<ItemDTO> items;

    public Mono<Double> getTotalSum() {
        return items.map(item -> item.getPrice() * item.getCount()).reduce(0.0, Double::sum);
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
