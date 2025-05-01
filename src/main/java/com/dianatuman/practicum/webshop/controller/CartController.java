package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import com.dianatuman.practicum.webshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String getCartItems(Model model) {
        List<ItemDTO> cartItems = itemService.getCartItems();
        model.addAttribute("items", cartItems);
        model.addAttribute("total", cartItems.stream().mapToDouble(item -> item.getPrice() * item.getCount()).sum());
        return "cart";
    }

    @PostMapping("/items/{itemId}")
    public String setItemCartCount(@PathVariable long itemId, @RequestParam String action) {
        itemService.setItemCartCount(itemId, action);
        return "redirect:/cart/items";
    }

    @PostMapping("/buy")
    public String buyCart(Model model) {
        Long orderId = orderService.createOrder(itemService.getCartItems());
        itemService.clearCart();
        model.addAttribute("order", orderService.getOrder(orderId));
        return String.format("redirect:/orders/%s?newOrder=true", orderId);
    }
}
