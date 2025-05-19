package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Controller
@RequestMapping("/items")
public class ItemsController {

    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Mono<String> getItems(Model model, @RequestParam(name = "search", defaultValue = "") String search,
                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                 @RequestParam(name = "sort", defaultValue = "") String sort) {
        var items = itemService.getItems(search,
                PageRequest.of(pageNumber, pageSize, sort.isEmpty() ? Sort.unsorted() : Sort.by(sort).ascending()));
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("items", items);
        return Mono.just("items");
    }

    @GetMapping("/{id}")
    public Mono<String> getItem(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("item", itemService.getItem(id));
        return Mono.just("item");
    }

    @PostMapping("/{id}")
    public String setItemCartCount(@PathVariable(name = "id") long id, @RequestParam String action) {
        itemService.setItemCartCount(id, action);
        return String.format("redirect:/items/%s", id);
    }

    @GetMapping("/add")
    public Mono<String> addItemPage() {
        return Mono.just("add-item");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> addItem(@RequestPart("itemName") String itemName, @RequestPart("description") String description,
                                @RequestPart("image") MultipartFile image, @RequestPart("price") String price) throws IOException {
        itemService.addItem(new ItemDTO(itemName, description, Double.valueOf(price)), image.getBytes());
        return Mono.just("redirect:/items");
    }

    @GetMapping("/{id}/edit")
    public Mono<String> editItemPage(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("item", itemService.getItem(id));
        return Mono.just("add-item");
    }

    @PostMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String editItem(@PathVariable(name = "id") long id, Model model,
                           @RequestPart("itemName") String itemName, @RequestPart("description") String description,
                           @RequestPart("image") MultipartFile image, @RequestPart("price") String price) throws IOException {
        itemService.editItem(id, new ItemDTO(itemName, description, Double.valueOf(price)), image.getBytes());
        model.addAttribute("item", itemService.getItem(id));
        return String.format("redirect:/items/%s", id);
    }
}
