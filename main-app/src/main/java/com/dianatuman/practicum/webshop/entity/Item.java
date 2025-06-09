package com.dianatuman.practicum.webshop.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("items")
public class Item {

    @Id
    private Long id;

    private String itemName;

    private String description;

    private Double price;

    private byte[] image;

    public Item(Long id) {
        this.id = id;
    }

    public Item(String itemName, String description, Double price) {
        this.description = description;
        this.price = price;
        this.itemName = itemName;
    }
}