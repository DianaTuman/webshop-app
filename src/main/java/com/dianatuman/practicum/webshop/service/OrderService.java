package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.entity.OrderItem;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.OrderItemRepository;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final ItemMapper itemMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(ItemMapper itemMapper, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.itemMapper = itemMapper;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderDTO getOrder(long orderId) {
        return mapOrderDTOFromEntity(orderRepository.getReferenceById(orderId));
    }

    public List<OrderDTO> getOrders() {
        return orderRepository.findAll().stream().map(this::mapOrderDTOFromEntity).toList();
    }

    public Long createOrder(List<ItemDTO> cartItems) {
        Order newOrder = orderRepository.save(new Order());
        List<OrderItem> list = cartItems.stream()
                .map(item -> new OrderItem(newOrder, new Item(item.getId()), item.getCount())).toList();
        orderItemRepository.saveAll(list);
        return newOrder.getId();
    }

    private OrderDTO mapOrderDTOFromEntity(Order orderEntity) {
        List<OrderItem> items = orderEntity.getItems();
        if (items != null) {
            List<ItemDTO> list = items.stream()
                    .map(orderItem -> {
                        ItemDTO item = itemMapper.toDTO(orderItem.getItem());
                        item.setCount(orderItem.getCount());
                        return item;
                    }).toList();
            return new OrderDTO(orderEntity.getId(), list);
        }
        return new OrderDTO(orderEntity.getId(), new ArrayList<>());
    }

}
