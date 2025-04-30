package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public byte[] getImage(long id) {
        return itemRepository.getReferenceById(id).getImage();
    }

    public List<ItemDTO> getItems() {
        return null;
    }

    public ItemDTO getItem(long id) {
        Item referenceById = itemRepository.getReferenceById(id);
        return null;
    }
}
