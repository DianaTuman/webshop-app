package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDTO getOrder(long id) {
        return null;
    }

    public List<OrderDTO> getOrders() {
        return new ArrayList<>();
    }

    public Long createOrder() {
//        orderRepository.save();
        return 1L;
    }
}
