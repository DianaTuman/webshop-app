package com.dianatuman.practicum.webshop.controller;

import org.springframework.stereotype.Controller;

@Controller
public class CartController {

//    г) GET "/cart/items" - список товаров в корзине
//    Возвращает:
//    шаблон "cart.html"
//    используется модель для заполнения шаблона:
//            "items" - List<Item> - список товаров в корзине (id, title, decription, imgPath, count, price)
//        			"total" - суммарная стоимость заказа
//        			"empty" - true, если в корзину не добавлен ни один товар
//    д) POST "/cart/items/{id}" - изменить количество товара в корзине
//    Параматры:
//    action - значение из перечисления PLUS|MINUS|DELETE (PLUS - добавить один товар, MINUS - удалить один товар, DELETE - удалить товар из корзины)
//    Возвращает:
//    редирект на "/cart/items"
//    з) POST "/buy" - купить товары в корзине (выполняет покупку товаров в корзине и очищает ее)
//    Возвращает:
//    редирект на "/orders/{id}?newOrder=true"


}
