package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/items")
public class ItemsController {

    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    //    б) GET "/main/items" - список всех товаров плиткой на главной странице
//    Параметры:
//    search - строка с поисков по названию/описанию товара (по умолчанию, пустая строка - все товары)
//    sort - сортировка перечисление NO, ALPHA, PRICE (по умолчанию, NO - не использовать сортировку)
//    pageSize - максимальное число товаров на странице (по умолчанию, 10)
//    pageNumber - номер текущей страницы (по умолчанию, 1)
//    Возвращает:
//    шаблон "items.html"
//    используется модель для заполнения шаблона:
//            "items" - List<List<Item>> - список товаров по N в ряд (id, title, decription, imgPath, count, price)
//            			"search" - строка поиска (по умолчанию, пустая строка - все товары)
//            			"sort" - сортировка перечисление NO, ALPHA, PRICE (по умолчанию, NO - не использовать сортировку)
//            			"paging":
//                                "pageNumber" - номер текущей страницы (по умолчанию, 1)
//            				"pageSize" - максимальное число товаров на странице (по умолчанию, 10)
//            				"hasNext" - можно ли пролистнуть вперед
//            				"hasPrevious" - можно ли пролистнуть назад
    @GetMapping
    @ResponseBody
    public String getItems(Model model, @RequestParam(value = "search", defaultValue = "") String search,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {
        return "items";
    }

    //    е) GET "/items/{id}" - карточка товара
//    Возвращает:
//    шаблон "item.html"
//    используется модель для заполнения шаблона:
//            "item" - товаров (id, title, decription, count, price)
    @GetMapping("/{id}")
    @ResponseBody
    public String getItem(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("item", itemService.getItem(id));
        return "item";
    }


    //    ж) POST "/items/{id}" - изменить количество товара в корзине
//    Параматры:
//    action - значение из перечисления PLUS|MINUS|DELETE (PLUS - добавить один товар, MINUS - удалить один товар, DELETE - удалить товар из корзины)
//    Возвращает:
//    редирект на "/items/{id}"
    @PostMapping("/{id}")
    @ResponseBody
    public String setItemCartCount(@PathVariable(name = "id") long id) {
        return String.format("redirect:/items/%s", id);
    }

}
