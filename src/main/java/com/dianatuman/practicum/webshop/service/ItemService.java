package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    private final Map<Long, Integer> cartItems = new HashMap<>();

    public ItemService(ItemMapper itemMapper, ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    public Mono<byte[]> getImage(long itemId) {
        return itemRepository.findById(itemId).map(Item::getImage);
    }

    public Mono<Page<ItemDTO>> getItems(String search, Pageable pageRequest) {
        return itemRepository.findByItemNameContainingIgnoreCase(search)
                .map(itemMapper::toDTO).map(this::setCount)
                .collectList()
                .map(items -> {
                    final int start = (int) pageRequest.getOffset();
                    final int end = Math.min((start + pageRequest.getPageSize()), items.size());
                    return new PageImpl<>(items.subList(start, end), pageRequest, items.size());
                });
    }

    public Mono<ItemDTO> getItem(long itemId) {
        return itemRepository.findById(itemId).map(itemMapper::toDTO).map(this::setCount);
    }

    public void addItem(ItemDTO itemDTO, byte[] bytes) {
        Item entity = itemMapper.toEntity(itemDTO);
        entity.setImage(bytes);
        itemRepository.save(entity);
    }

    public void editItem(long itemId, ItemDTO itemDTO, byte[] bytes) {
        itemDTO.setId(itemId);
        Item entity = itemMapper.toEntity(itemDTO);
        if (bytes != null) {
            entity.setImage(bytes);
        }
        itemRepository.save(entity);
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

    public Flux<ItemDTO> getCartItems() {
        return itemRepository.findAllById(cartItems.keySet())
                .map(itemMapper::toDTO).map(this::setCount);
    }

    public void clearCart() {
        cartItems.clear();
    }

    private ItemDTO setCount(ItemDTO dto) {
        if (cartItems.containsKey(dto.getId())) {
            dto.setCount(cartItems.get(dto.getId()));
        }
        return dto;
    }
}
