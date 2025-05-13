package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItem.OrderItemKey> {
}
