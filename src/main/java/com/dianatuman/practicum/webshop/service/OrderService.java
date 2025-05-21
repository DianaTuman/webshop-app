package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrderService {

    private final ItemMapper itemMapper;
    private final OrderRepository orderRepository;

    public OrderService(ItemMapper itemMapper, OrderRepository orderRepository) {
        this.itemMapper = itemMapper;
        this.orderRepository = orderRepository;
    }

    public Mono<OrderDTO> getOrder(long orderId) {
        return orderRepository.findById(orderId).flatMap(this::mapOrderDTOFromEntity);
    }

    public Flux<OrderDTO> getOrders() {
        return orderRepository.findAll().flatMap(this::mapOrderDTOFromEntity).sort();
    }

    private Mono<OrderDTO> mapOrderDTOFromEntity(Order orderEntity) {
        return Mono.zip(objects -> {
            Order order = (Order) objects[0];
            List<ItemDTO> orderItems = (List<ItemDTO>) objects[1];
            return new OrderDTO(order.getId(), orderItems);
        }, Mono.just(orderEntity), orderRepository.getOrderItems(orderEntity.getId())
                .map(itemMapper::orderItemToDTO).collectList());
    }
}
