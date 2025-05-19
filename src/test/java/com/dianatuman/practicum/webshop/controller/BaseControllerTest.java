package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.service.ItemService;
import com.dianatuman.practicum.webshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

public class BaseControllerTest {

    @Autowired
    protected WebTestClient webTestClient;

    @MockitoBean
    protected ItemService itemService;

    @MockitoBean
    protected OrderService orderService;
}
