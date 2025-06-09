package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.dto.OrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

@WebFluxTest(OrdersController.class)
public class OrderControllerTest extends BaseControllerTest {

    @Test
    public void getOrders_shouldReturnOrdersPage() {
        when(orderService.getOrders()).thenReturn(Flux.just(new OrderDTO(1L, List.of())));
        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody()
                .xpath("//h2/a").isEqualTo("Order #1")
                .xpath("//a[@href='/orders/1?newOrder=false']").exists();
        verify(orderService, times(1)).getOrders();
    }

    @Test
    public void getOrdersId_shouldReturnOrderPage() {
        ItemDTO e1 = new ItemDTO("TestItem", "Item Description", 10.0);
        ItemDTO e2 = new ItemDTO("TestItem2", "Item Descr", 12.0);
        e1.setCount(1);
        e2.setCount(1);
        when(orderService.getOrder(1)).thenReturn(Mono.just(new OrderDTO(1L, List.of(e1, e2))));

        webTestClient.get()
                .uri("/orders/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody()
                .xpath("//h2").isEqualTo("Order #1")
                .xpath("//h3").isEqualTo("Total: 22.0 $");
        verify(orderService, times(1)).getOrder(1);
    }
}
