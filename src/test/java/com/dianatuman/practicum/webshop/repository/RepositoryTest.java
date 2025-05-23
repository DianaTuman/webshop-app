package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataR2dbcTest
@ActiveProfiles("test")
public class RepositoryTest {

    @Autowired
    protected ItemRepository itemRepository;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @Test
    public void findItemByItemNameTest() {
        var item1 = new Item("TestItem", "Desc", 11.0);
        var item2 = new Item("ItemName", "Desc", 11.0);

        Iterable<Item> test = itemRepository.saveAll(List.of(item1, item2))
                .thenMany(itemRepository.findByItemNameContainingIgnoreCase("TestItem", Sort.unsorted()))
                .toIterable();

        assertThat(test)
                .isNotEmpty()
                .hasSize(1)
                .first()
                .extracting(Item::getItemName).isEqualTo(item1.getItemName());
    }
}
