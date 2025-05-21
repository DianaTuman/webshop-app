package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BaseServiceTest {

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ItemMapper itemMapper;

    @Autowired
    protected WebTestClient webTestClient;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");
}
