package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    private Map<Long, Integer> cartItems = new HashMap<>();

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public byte[] getImage(long itemId) {
        return itemRepository.getReferenceById(itemId).getImage();
    }

    public List<ItemDTO> getItems() {
        itemRepository.findAll();
        return null;
    }

    public ItemDTO getItem(long itemId) {
        Item referenceById = itemRepository.getReferenceById(itemId);
        return null;
    }

    public void addItem(ItemDTO itemDTO) {
//        itemRepository.save();
    }

    public void editItem(long itemId, ItemDTO itemDTO) {
        itemDTO.setId(itemId);
//        itemRepository.save();
    }

    public void deleteItem(long itemId) {
        itemRepository.deleteById(itemId);
    }

    public void setItemCartCount(long itemId, String action) {

    }

    public List<ItemDTO> getCartItems() {
        List<Item> entities = itemRepository.findAllById(cartItems.keySet());
        return null;
    }
}
