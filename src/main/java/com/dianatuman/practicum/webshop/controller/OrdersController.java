package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Mono<String> getOrders(Model model) {
        model.addAttribute("orders", orderService.getOrders());
        return Mono.just("orders");
    }

    @GetMapping("/{id}")
    public Mono<String> getOrder(@PathVariable long id, Model model) {
        model.addAttribute("order", orderService.getOrder(id));
        return Mono.just("order");
    }
}
