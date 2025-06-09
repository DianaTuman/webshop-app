package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        var itemsIds = itemService.getItemsIds(search, sort.isEmpty() ? Sort.unsorted() : Sort.by(sort).ascending());
        var itemsPage = itemsIds.flatMap(itemService::getItem)
                .map(itemService::setCount)
                .collectList()
                .map(items -> {
                    final int start = (int) pageRequest.getOffset();
                    final int end = Math.min((start + pageRequest.getPageSize()), items.size());
                    return new PageImpl<>(items.subList(start, end), pageRequest, items.size());
                });
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("items", itemsPage);
        return Mono.just("items");
    }

    @GetMapping("/{id}")
    public Mono<String> getItem(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("item", itemService.getItem(id).map(itemService::setCount));
        return Mono.just("item");
    }

    @PostMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> setItemCartCount(@PathVariable(name = "id") long id, @RequestPart("action") String action) {
        itemService.setItemCartCount(id, action);
        return Mono.just(String.format("redirect:/items/%s", id));
    }

    @GetMapping("/add")
    public Mono<String> addItemPage() {
        return Mono.just("add-item");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<String> addItem(@RequestPart("itemName") String itemName, @RequestPart("description") String description,
                                @RequestPart("image") Mono<FilePart> image, @RequestPart("price") String price) {
        return itemService.addItem(new ItemDTO(itemName, description, Double.valueOf(price)), image)
                .map(saved -> String.format("redirect:/items/%s", saved.getId()));
    }
}
