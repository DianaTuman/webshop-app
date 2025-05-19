package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.entity.OrderItem;
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

    public Mono<OrderDTO> getOrder(long orderId) {
        return orderRepository.findById(orderId).map(this::mapOrderDTOFromEntity);
    }

    public Flux<OrderDTO> getOrders() {
        return orderRepository.findAll().map(this::mapOrderDTOFromEntity);
    }

    public Mono<OrderDTO> createOrder(Flux<ItemDTO> cartItems) {
        var cart = cartItems.map(item -> new OrderItem(new Item(item.getId()), item.getCount()));
        return orderRepository.createNewOrder(cart).map(this::mapOrderDTOFromEntity);
    }

    private OrderDTO mapOrderDTOFromEntity(Order orderEntity) {
        var items = orderRepository.getOrderItems(orderEntity.getId());
        var list = items
                .map(orderItem -> {
                    ItemDTO item = itemMapper.toDTO(orderItem.getItem());
                    item.setCount(orderItem.getCount());
                    return item;
                });
        return new OrderDTO(orderEntity.getId(), list);
    }
}
