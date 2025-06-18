package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(ItemsController.class)
public class ItemControllerTest extends BaseControllerTest {

    @Test
    public void getItems_shouldReturnItemsPage() {
        ItemDTO e1 = new ItemDTO("TestItem", "Item Description", 10.0);
        e1.setId(1L);
        when(itemService.getItemsIds(any(), any())).thenReturn(Flux.just(1L));
        when(itemService.getItem(1L)).thenReturn(Mono.just(e1));
        when(itemService.setCount(e1, "user")).thenReturn(e1);

        webTestClient.get()
                .uri("/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody()
                .xpath("//a[@href='/items/1']").exists();
        verify(itemService, times(1)).getItemsIds(any(), any());
    }

    @Test
    @WithMockUser
    public void getItemId_shouldReturnItemPage() {
        ItemDTO e1 = new ItemDTO("TestItem", "Item Description", 10.0);
        e1.setId(1L);
        e1.setCount(1);
        when(itemService.getItem(1)).thenReturn(Mono.just(e1));
        when(itemService.setCount(e1, "user")).thenReturn(e1);

        webTestClient.get()
                .uri("/items/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody()
                .xpath("//div/p/b").isEqualTo("TestItem")
                .xpath("//div/p/b[2]").isEqualTo("10.0 $")
                .xpath("//div/p/span").isEqualTo("Item Description");
        verify(itemService, times(1)).getItem(1);
    }

}
