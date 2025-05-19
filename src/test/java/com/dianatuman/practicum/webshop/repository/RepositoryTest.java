package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class RepositoryTest {

    @Autowired
    protected ItemRepository itemRepository;

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
    public void findItemByItemNameTest() {
        var item1 = itemRepository.save(new Item("TestItem", "Desc", 11.0));
        itemRepository.save(new Item("ItemName", "Desc", 11.0));

        var found = itemRepository.findByItemNameContainingIgnoreCase("test").collectList().block();

        assertThat(found).isNotNull();
        assertThat(found).hasSize(1);
        assertThat(found.getFirst()).isEqualTo(item1);
    }
}
