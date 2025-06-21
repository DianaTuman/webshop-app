package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Set;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ItemService itemService;
    private final WebClient paymentWebClient;
    private Mono<Double> cartTotal = Mono.just(0.0);

    public CartController(ItemService itemService, WebClient paymentWebClient) {
        this.itemService = itemService;
        this.paymentWebClient = paymentWebClient;
    }

    @GetMapping("/items")
    public Mono<String> getCartItems(Model model, Principal principal) {
        Set<Long> cartItemsIds = itemService.getCartItems(principal.getName());
        Flux<ItemDTO> cartItems = Flux.concat(cartItemsIds.stream().map(itemService::getItem).toList())
                .map(itemDTO -> itemService.setCount(itemDTO, principal.getName()));
        model.addAttribute("items", cartItems.collectList());
        cartTotal = cartItems.map(item -> item.getPrice() * item.getCount()).reduce(0.0, Double::sum);
        Mono<Double> balance = paymentWebClient.get().uri("/balance")
                .retrieve().bodyToMono(Double.class)
                .onErrorReturn(-1.0);
        Mono<Boolean> canOrder = Mono.zip(cartTotal, balance)
                .map(((orderTotal) -> (orderTotal.getT2() >= orderTotal.getT1())));
        model.addAttribute("canOrder", canOrder);
        model.addAttribute("total", cartTotal);
        return Mono.just("cart");
    }

    @PostMapping(path = "/items/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> setItemCartCount(Principal principal, @PathVariable long itemId, @RequestPart("action") String action) {
        itemService.setItemCartCount(itemId, action, principal.getName());
        return Mono.just("redirect:/cart/items");
    }

    @PostMapping("/buy")
    public Mono<String> buyCart(Principal principal) {
        return paymentWebClient.put().uri("/balance")
                .body(cartTotal, Double.class)
                .retrieve().onStatus(
                        HttpStatus.BAD_REQUEST::equals,
                        response -> response.bodyToMono(String.class).map(NoSuchElementException::new))
                .toBodilessEntity()
                .flatMap(response ->
                        itemService.createOrder(principal.getName())
                                .map(orderId -> String.format("redirect:/orders/%s?newOrder=true", orderId)))
                .onErrorReturn("redirect:/cart/items?failed=true");
    }
}
