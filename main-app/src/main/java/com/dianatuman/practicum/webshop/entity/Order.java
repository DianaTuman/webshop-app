package com.dianatuman.practicum.webshop.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("orders")
public class Order {

    @Id
    private Long id;

    private String username;

    public Order(String username) {
        this.username = username;
    }
}
