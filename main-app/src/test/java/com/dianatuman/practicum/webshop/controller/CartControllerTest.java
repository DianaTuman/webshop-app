package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.mockito.Mockito.*;

@WebFluxTest(CartController.class)
public class CartControllerTest extends BaseControllerTest {

    @Test
    public void getCartItems_shouldReturnCartPage() {
        ItemDTO e1 = new ItemDTO("TestItem", "Item Description", 10.0);
        e1.setId(1L);
        e1.setCount(2);
        when(itemService.getCartItems()).thenReturn(Set.of(1L));
        when(itemService.getItem(1L)).thenReturn(Mono.just(e1));
        when(itemService.setCount(e1)).thenReturn(e1);

        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody()
                .xpath("//table[2]/tr[2]/td/b").isEqualTo("Total: 20.0 $");
        verify(itemService, times(1)).getCartItems();
    }
}
