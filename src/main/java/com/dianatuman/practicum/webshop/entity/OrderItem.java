package com.dianatuman.practicum.webshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
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

    int count;

    public OrderItem(Order order, Item item, int count) {
        this.order = order;
        this.item = item;
        this.count = count;
        this.id = new OrderItemKey(order.getId(), item.getId());
    }

    @Embeddable
    @NoArgsConstructor
    public static class OrderItemKey implements Serializable {

        @Column(name = "order_id")
        Long orderId;

        @Column(name = "item_id")
        Long itemId;

        public OrderItemKey(Long orderId, Long itemId) {
            this.orderId = orderId;
            this.itemId = itemId;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            OrderItemKey that = (OrderItemKey) o;
            return Objects.equals(orderId, that.orderId) && Objects.equals(itemId, that.itemId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderId, itemId);
        }
    }
}

