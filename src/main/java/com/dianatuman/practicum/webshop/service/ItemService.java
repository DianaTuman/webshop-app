package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Autowired
    ItemMapper itemMapper;

    private final Map<Long, Integer> cartItems = new HashMap<>();

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public byte[] getImage(long itemId) {
        return itemRepository.getReferenceById(itemId).getImage();
    }

    public List<ItemDTO> getItems() {
        return getDTOs(itemRepository.findAll());
    }

    public ItemDTO getItem(long itemId) {
        ItemDTO dto = itemMapper.toDTO(itemRepository.getReferenceById(itemId));
        setCount(dto);
        return dto;
    }

    public void addItem(ItemDTO itemDTO) {
        itemRepository.save(itemMapper.toEntity(itemDTO));
    }

    public void editItem(long itemId, ItemDTO itemDTO) {
        itemDTO.setId(itemId);
        itemRepository.save(itemMapper.toEntity(itemDTO));
    }

    public void deleteItem(long itemId) {
        itemRepository.deleteById(itemId);
    }

    public void setItemCartCount(long itemId, String action) {
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

    public List<ItemDTO> getCartItems() {
        return getDTOs(itemRepository.findAllById(cartItems.keySet()));
    }

    public void clearCart() {
        cartItems.clear();
    }

    private List<ItemDTO> getDTOs(List<Item> entities) {
        List<ItemDTO> list = entities.stream().map(entity -> itemMapper.toDTO(entity)).toList();
        list.forEach(this::setCount);
        return list;
    }

    private void setCount(ItemDTO dto) {
        if (cartItems.containsKey(dto.getId())) {
            dto.setCount(cartItems.get(dto.getId()));
        }
    }
}
