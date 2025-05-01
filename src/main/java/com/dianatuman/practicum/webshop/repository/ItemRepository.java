package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByItemNameContainingIgnoreCase(String itemName, Pageable pageRequest);
}
