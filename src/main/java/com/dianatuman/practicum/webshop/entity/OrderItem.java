package com.dianatuman.practicum.webshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItem {

    Item item;

    int count;
}

