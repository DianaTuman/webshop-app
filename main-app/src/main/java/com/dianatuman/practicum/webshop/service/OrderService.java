package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final ItemMapper itemMapper;
    private final OrderRepository orderRepository;

    public OrderService(ItemMapper itemMapper, OrderRepository orderRepository) {
        this.itemMapper = itemMapper;
        this.orderRepository = orderRepository;
    }

    public Mono<OrderDTO> getOrderForUser(long orderId, String username) {
        return orderRepository.findByIdAndUsername(orderId, username).flatMap(this::mapOrderDTOFromEntity);
    }

    public Flux<OrderDTO> getOrdersByUsername(String username) {
        return orderRepository.findByUsername(username).flatMap(this::mapOrderDTOFromEntity).sort();
    }

    private Mono<OrderDTO> mapOrderDTOFromEntity(Order orderEntity) {
        return Mono.zip(Mono.just(orderEntity),
                        orderRepository.getOrderItems(orderEntity.getId()).map(itemMapper::orderItemToDTO).collectList())
                .map(tuple -> new OrderDTO(tuple.getT1().getId(), tuple.getT2(), tuple.getT1().getUsername()));
    }
}
