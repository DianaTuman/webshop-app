package com.dianatuman.practicum.webshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "orders_items")
public class OrderItem {

    @EmbeddedId
    OrderItemKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    Item item;

    @Column(columnDefinition = "integer default 1")
    int count;
}

@Embeddable
class OrderItemKey implements Serializable {

    @Column(name = "order_id")
    Long orderId;

    @Column(name = "item_id")
    Long itemId;
}
