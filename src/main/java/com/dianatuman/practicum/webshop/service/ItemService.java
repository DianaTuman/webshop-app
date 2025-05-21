package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    private final Map<Long, Integer> cartItems = new HashMap<>();

    public ItemService(ItemMapper itemMapper, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    public Mono<byte[]> getImage(long itemId) {
        return itemRepository.findById(itemId).map(Item::getImage);
    }

    public Mono<Page<ItemDTO>> getItems(String search, Pageable pageRequest, Sort sort) {
        return itemRepository.findByItemNameContainingIgnoreCase(search, sort)
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

    public Mono<Item> addItem(ItemDTO itemDTO, Mono<FilePart> image) {
        return Mono.zip(objects -> {
                            Item item = (Item) objects[0];
                            var filePart = (DataBuffer) objects[1];
                            item.setImage(filePart.toByteBuffer().array());
                            return item;
                        },
                        itemRepository.save(itemMapper.toEntity(itemDTO)),
                        image.flatMap(filePart -> DataBufferUtils.join(filePart.content())))
                .flatMap(itemRepository::save);
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

    private ItemDTO setCount(ItemDTO dto) {
        if (cartItems.containsKey(dto.getId())) {
            dto.setCount(cartItems.get(dto.getId()));
        }
        return dto;
    }

    public Mono<Long> createOrder() {
        return orderRepository.save(new Order())
                .flatMapMany(order ->
                        Flux.concat(
                                Stream.concat(
                                        cartItems.entrySet().parallelStream().map((entry) ->
                                                orderRepository.createNewOrderItem(order.getId(), entry.getKey(), entry.getValue())),
                                        Stream.of(Mono.just(order.getId()))
                                ).toList()))
                .doOnComplete(cartItems::clear).last();
    }
}
