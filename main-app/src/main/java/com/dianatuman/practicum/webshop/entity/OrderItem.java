package com.dianatuman.practicum.webshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItem {

    private Long id;

    private String itemName;

    private String description;

    private Double price;

    private int count;

}

