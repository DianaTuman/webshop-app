package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.CartDTO;
import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    private final Map<String, CartDTO> cartItems = new HashMap<>();

    public ItemService(ItemMapper itemMapper, ItemRepository itemRepository, OrderRepository orderRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
    }

    public Mono<byte[]> getImage(long itemId) {
        return itemRepository.findById(itemId).map(Item::getImage);
    }

    public Flux<ItemDTO> getItems(String search, Sort sort) {
        return itemRepository.findByItemNameContainingIgnoreCase(search, sort)
                .map(itemMapper::toDTO);
    }

    @Cacheable(value = "itemsIds")
    public Flux<Long> getItemsIds(String search, Sort sort) {
        System.out.println("CACHING itemsIds " + search + " " + sort.toString());
        return itemRepository.findIdByItemNameContainingIgnoreCase(search, sort);
    }

    @Cacheable(value = "item", key = "#itemId")
    public Mono<ItemDTO> getItem(long itemId) {
        System.out.println("CACHING " + itemId);
        return itemRepository.findById(itemId).map(itemMapper::toDTO);
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

    public void setItemCartCount(long itemId, String action, String username) {
        checkUsername(username);
        cartItems.get(username).setItemCartCount(itemId, action);
    }

    public Set<Long> getCartItems(String username) {
        checkUsername(username);
        return cartItems.get(username).getCartItems().keySet();
    }

    private void checkUsername(String username) {
        if (!cartItems.containsKey(username)) {
            cartItems.put(username, new CartDTO());
        }
    }

    public ItemDTO setCount(ItemDTO dto, String username) {
        checkUsername(username);
        if (cartItems.get(username).getCartItems().containsKey(dto.getId())) {
            dto.setCount(cartItems.get(username).getCartItems().get(dto.getId()));
        }
        return dto;
    }

    public Mono<Long> createOrder(String username) {
        return orderRepository.save(new Order(username))
                .flatMapMany(order ->
                        Flux.concat(Stream.concat(
                                cartItems.get(username).getCartItems().entrySet()
                                        .parallelStream().map((entry) ->
                                                orderRepository.createNewOrderItem(order.getId(),
                                                        entry.getKey(), entry.getValue())),
                                Stream.of(Mono.just(order.getId()))
                        ).toList()))
                .doOnComplete(() -> cartItems.get(username).clearCart()).last();
    }
}
