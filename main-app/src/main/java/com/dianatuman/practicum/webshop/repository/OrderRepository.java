package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.entity.OrderItem;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    @Query("SELECT items.id, items.description, items.item_name, items.price, orders_items.count " +
            "FROM public.items INNER JOIN public.orders_items ON items.id = orders_items.item_id " +
            "WHERE orders_items.order_id =:orderId")
    Flux<OrderItem> getOrderItems(Long orderId);

    @Modifying
    @Query(value = "INSERT INTO orders_items (order_id, item_id, count) VALUES (:orderId, :itemId, :count) RETURNING order_id")
    Mono<Long> createNewOrderItem(Long orderId, Long itemId, int count);
}
