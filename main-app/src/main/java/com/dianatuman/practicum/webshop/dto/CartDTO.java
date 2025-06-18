package com.dianatuman.practicum.webshop.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CartDTO {

    private final Map<Long, Integer> cartItems = new HashMap<>();

    public void setItemCartCount(Long itemId, String action) {
        switch (action) {
            case "plus" -> cartItems.merge(itemId, 1, Integer::sum);
            case "minus" -> {
                if (cartItems.containsKey(itemId)) {
                    Integer count = cartItems.get(itemId);
                    if (count - 1 == 0) {
                        cartItems.remove(itemId);
                    } else {
                        cartItems.replace(itemId, count - 1);
                    }
                }
            }
            case "delete" -> cartItems.remove(itemId);
        }
    }

    public void clearCart() {
        cartItems.clear();
    }
}
