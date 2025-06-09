package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ItemRepository extends R2dbcRepository<Item, Long> {

    Flux<Item> findByItemNameContainingIgnoreCase(String itemName, Sort sort);

    Flux<Long> findIdByItemNameContainingIgnoreCase(String itemName, Sort sort);
}
