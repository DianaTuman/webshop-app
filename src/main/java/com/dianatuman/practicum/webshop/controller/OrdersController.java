package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }


    //    GET "/orders" - список заказов
//    Возвращает:
//    шаблон "orders.html"
//    используется модель для заполнения шаблона:
//            "orders" - List<Order> - список заказов
//        				"id" - идентификатор заказа
//        				"items" - List<Item> - список товаров в заказе (id, title, decription, imgPath, count, price)
    @GetMapping
    @ResponseBody
    public String getOrders(Model model) {
        model.addAttribute("orders", orderService.getOrders());
        return "orders";
    }

    //
//
//    GET "/orders/{id}" - карточка заказа
//    Параматры:
//    newOrder - true, если переход со страницы оформления заказа (по умолчанию, false)
//    Возвращает:
//    шаблон "order.html"
//    используется модель для заполнения шаблона:
//            "order" - заказ Order
//       					"id" - идентификатор заказа
//        				"items" - List<Item> - список товаров в заказе (id, title, decription, imgPath, count, price)
//        			"newOrder" - true, если переход со страницы оформления заказа (по умолчанию, false)
    @GetMapping("/{id}")
    @ResponseBody
    public String getOrder(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("order", orderService.getOrder(id));
        return "order";
    }
}
