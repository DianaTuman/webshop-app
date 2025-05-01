package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    // "sort" - сортировка перечисление NO, ALPHA, PRICE (по умолчанию, NO - не использовать сортировку)
    @GetMapping
    public String getItems(Model model, @RequestParam(value = "search", defaultValue = "") String search,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {
        Page<ItemDTO> items = itemService.getItems(search, PageRequest.of(pageNumber, pageSize));
        model.addAttribute("search", search);
        model.addAttribute("paging", items.getPageable());
        model.addAttribute("items", items);
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
}
