package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ItemService itemService;

    public HomeController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String homePage() {
        return "redirect:./items";
    }

    @GetMapping("/images/{id}")
    @ResponseBody
    public byte[] getImage(@PathVariable(name = "id") long id) {
        return itemService.getImage(id);
    }

}
