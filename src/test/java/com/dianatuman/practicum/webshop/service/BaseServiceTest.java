package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.WebshopAppApplication;
import com.dianatuman.practicum.webshop.mapper.ItemMapper;
import com.dianatuman.practicum.webshop.repository.ItemRepository;
import com.dianatuman.practicum.webshop.repository.OrderItemRepository;
import com.dianatuman.practicum.webshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(classes = WebshopAppApplication.class)
@ActiveProfiles("test")
public class BaseServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderItemRepository orderItemRepository;

    @Autowired
    protected ItemMapper itemMapper;

    protected MockMvc mockMvc;

    private static final PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>("postgres:17")
                .withDatabaseName("testdb")
                .withUsername("junit")
                .withPassword("junit");
        postgres.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        itemRepository.deleteAll();
    }
}
