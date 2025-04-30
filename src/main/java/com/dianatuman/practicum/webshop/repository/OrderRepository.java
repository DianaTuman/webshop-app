package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
