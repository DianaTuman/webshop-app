package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.service.ItemService;
import com.dianatuman.practicum.webshop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final OrderService orderService;

    private final ItemService itemService;

    public CartController(OrderService orderService, ItemService itemService) {
        this.orderService = orderService;
        this.itemService = itemService;
    }

    //    г) GET "/cart/items" - список товаров в корзине
//    Возвращает:
//    шаблон "cart.html"
//    используется модель для заполнения шаблона:
//            "items" - List<Item> - список товаров в корзине (id, title, decription, imgPath, count, price)
//        			"total" - суммарная стоимость заказа
//        			"empty" - true, если в корзину не добавлен ни один товар
    @GetMapping("/items")
    @ResponseBody
    public String getCartItems(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("item", itemService.getItem(id));
        return "item";
    }


    //    д) POST "/cart/items/{id}" - изменить количество товара в корзине
//    Параматры:
//    action - значение из перечисления PLUS|MINUS|DELETE (PLUS - добавить один товар, MINUS - удалить один товар, DELETE - удалить товар из корзины)
//    Возвращает:
//    редирект на "/cart/items"
    @PostMapping("/items/{id}")
    @ResponseBody
    public String setItemCartCount(@PathVariable(name = "id") long id) {
        return "redirect:/cart/items";
    }


//    з) POST "/buy" - купить товары в корзине (выполняет покупку товаров в корзине и очищает ее)
//    Возвращает:
//    редирект на "/orders/{id}?newOrder=true"

    @PostMapping("/buy")
    @ResponseBody
    public String buyCart() {
        Long orderId = orderService.createOrder();
        return String.format("redirect:/orders/%s?newOrder=true", orderId);
    }
}
