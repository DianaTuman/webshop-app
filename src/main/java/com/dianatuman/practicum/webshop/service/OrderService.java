package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDTO getOrder(long orderId) {
        Optional<Order> entity = orderRepository.findById(orderId);
        return null;
    }

    public List<OrderDTO> getOrders() {
        Iterable<Order> all = orderRepository.findAll();
        return new ArrayList<>();
    }

    public Long createOrder(List<ItemDTO> cartItems) {
        Order newOrder = new Order();
        return orderRepository.save(newOrder).getId();
    }
}
