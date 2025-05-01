package com.dianatuman.practicum.webshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDTO {

    private Long id;

    private String itemName;

    private String description;

    private Double price;

    private Integer count = 0;

    private byte[] image;

    public ItemDTO(String itemName, String description, Double price, byte[] image) {
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.image = image;
    }
}
