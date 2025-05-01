package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<ItemDTO> getItems(String search, Pageable pageRequest) {
        return itemRepository.findByItemNameContainingIgnoreCase(search, pageRequest).map(itemMapper::toDTO);
    }

    public ItemDTO getItem(long itemId) {
        return setCount(itemMapper.toDTO(itemRepository.getReferenceById(itemId)));
    }

    public void addItem(ItemDTO itemDTO) {
        itemRepository.save(itemMapper.toEntity(itemDTO));
    }

    public void editItem(long itemId, ItemDTO itemDTO) {
        itemDTO.setId(itemId);
        itemRepository.save(itemMapper.toEntity(itemDTO));
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
        return entities.stream().map(entity -> itemMapper.toDTO(entity)).map(this::setCount).toList();
    }

    private ItemDTO setCount(ItemDTO dto) {
        if (cartItems.containsKey(dto.getId())) {
            dto.setCount(cartItems.get(dto.getId()));
        }
        return dto;
    }
}
