package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.Item;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemRepositoryTest extends RepositoryBaseTest {

    @Test
    public void expectSavedItemIsFound() {
        Item item1 = new Item();
        item1.setItemName("THIS");
        itemRepository.save(item1);

        Item found = itemRepository.findByItemNameLike("TH%").getFirst();

        assertThat(found).isNotNull();
        assertThat(found.getItemName()).isEqualTo("THIS");
    }
}
