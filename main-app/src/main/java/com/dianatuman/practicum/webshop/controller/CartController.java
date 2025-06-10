package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ItemService itemService;

    private final WebClient paymentClient;

    private Mono<Double> cartTotal;

    public CartController(ItemService itemService) {
        this.itemService = itemService;
        paymentClient = WebClient.create("http://localhost:8081");
    }

    @GetMapping("/items")
    public Mono<String> getCartItems(Model model) {
        Set<Long> cartItemsIds = itemService.getCartItems();
        Flux<ItemDTO> cartItems = Flux.concat(cartItemsIds.stream().map(itemService::getItem).toList())
                .map(itemService::setCount);
        model.addAttribute("items", cartItems.collectList());
        cartTotal = cartItems.map(item -> item.getPrice() * item.getCount()).reduce(0.0, Double::sum);
        Mono<Double> balance = paymentClient.get().uri("/balance").retrieve().bodyToMono(Double.class)
                .onErrorReturn(-1.0);
        Mono<Boolean> canOrder = Mono.zip(cartTotal, balance)
                .map(((orderTotal) -> (orderTotal.getT2() >= orderTotal.getT1())));
        model.addAttribute("canOrder", canOrder);
        model.addAttribute("total", cartTotal);
        return Mono.just("cart");
    }

    @PostMapping(path = "/items/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> setItemCartCount(@PathVariable long itemId, @RequestPart("action") String action) {
        itemService.setItemCartCount(itemId, action);
        return Mono.just("redirect:/cart/items");
    }

    @PostMapping("/buy")
    public Mono<String> buyCart() {
        return paymentClient.put().uri("/balance")
                .body(cartTotal, Double.class)
                .retrieve().toBodilessEntity()
                .flatMap(response ->
                        itemService.createOrder().map(orderId -> String.format("redirect:/orders/%s?newOrder=true", orderId)))
                .onErrorReturn("redirect:/cart/items?failed=true");
    }
}
