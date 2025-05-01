package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public String getItems(Model model, @RequestParam(value = "search", defaultValue = "") String search,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber) {
        model.addAttribute("items", itemService.getItems());
        return "items";
    }

    @GetMapping("/{id}")
    public String getItem(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("item", itemService.getItem(id));
        return "item";
    }

    @PostMapping("/{id}")
    public String setItemCartCount(@PathVariable(name = "id") long id, @RequestParam String action) {
        itemService.setItemCartCount(id, action);
        return String.format("redirect:/items/%s", id);
    }

    @GetMapping("/add")
    public String addItemPage() {
        return "add-item";
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addItem(@RequestPart("itemName") String itemName, @RequestPart("description") String description,
                          @RequestPart("image") MultipartFile image, @RequestPart("price") String price) throws IOException {
        itemService.addItem(new ItemDTO(itemName, description, Double.valueOf(price), image.getBytes()));
        return "redirect:/items";
    }

    @GetMapping("/{id}/edit")
    public String editItemPage(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("item", itemService.getItem(id));
        return "add-item";
    }

    @PostMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String editItem(@PathVariable(name = "id") long id, Model model,
                           @RequestPart("itemName") String itemName, @RequestPart("description") String description,
                           @RequestPart("image") MultipartFile image, @RequestPart("price") String price) throws IOException {
        itemService.editItem(id, new ItemDTO(itemName, description, Double.valueOf(price), image.getBytes()));
        model.addAttribute("item", itemService.getItem(id));
        return String.format("redirect:/items/%s", id);
    }

    @PostMapping("/{id}/delete")
    public String deleteItem(@PathVariable(name = "id") long id) {
        itemService.deleteItem(id);
        return "redirect:/items";
    }

}
