package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.entity.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryTest {

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected OrderRepository orderRepository;

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

    @Test
    public void expectSavedItemIsFound() {
        Item item1 = new Item();
        item1.setItemName("THIS");
        itemRepository.save(item1);

//        Item found = itemRepository.findByItemNameContainingIgnoreCase();

//        assertThat(found).isNotNull();
//        assertThat(found.getItemName()).isEqualTo("THIS");
    }

    @Test
    public void saveOrder() {
        Item item1 = new Item();
        item1.setItemName("THIS");
        item1 = itemRepository.save(item1);
        Order newOrder = new Order();
        Item finalItem = item1;
        List<OrderItem> list = List.of(new OrderItem(newOrder, new Item(finalItem.getId()), 1));
        newOrder.setItems(list);

        Order referenceById = orderRepository.save(newOrder);
        assertThat(referenceById.getItems().getFirst().getItem().getItemName()).isEqualTo("THIS");
    }
}
