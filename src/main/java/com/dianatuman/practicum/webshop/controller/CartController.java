package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import com.dianatuman.practicum.webshop.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final OrderService orderService;

    private final ItemService itemService;

    public CartController(OrderService orderService, ItemService itemService) {
        this.orderService = orderService;
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public Mono<String> getCartItems(Model model) {
        Flux<ItemDTO> cartItems = itemService.getCartItems();
        model.addAttribute("items", cartItems.collectList());
        model.addAttribute("total",
                cartItems.map(item -> item.getPrice() * item.getCount()).reduce(0.0, Double::sum));
        return Mono.just("cart");
    }

    @PostMapping(path = "/items/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> setItemCartCount(@PathVariable long itemId, @RequestPart("action") String action) {
        itemService.setItemCartCount(itemId, action);
        return Mono.just("redirect:/cart/items");
    }

    @PostMapping("/buy")
    public Mono<String> buyCart() {
        var order = itemService.createOrder();
        return order.map(orderId -> String.format("redirect:/orders/%s?newOrder=true", orderId));
    }
}
