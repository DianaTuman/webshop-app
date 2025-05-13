package com.dianatuman.practicum.webshop.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemDTO itemDTO = (ItemDTO) o;
        return Objects.equals(itemName, itemDTO.itemName) && Objects.equals(description, itemDTO.description) && Objects.equals(price, itemDTO.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, description, price);
    }
}
