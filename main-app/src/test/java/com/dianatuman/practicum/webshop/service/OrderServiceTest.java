package com.dianatuman.practicum.webshop.service;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

// The DB will be prefilled with the data from the data.sql script
public class OrderServiceTest extends BaseServiceTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @Container
    @ServiceConnection
    static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"));

    @Test
    public void getOrdersTest() {
        webTestClient.get().uri("/orders")
                .exchange().expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .xpath("//h2/a[text()='Order #100']").exists()
                .xpath("//h2/a[text()='Order #200']").exists()
                .xpath("//h2/a[text()='Order #300']").exists();
    }

    @Test
    public void getOrderTest() {
        webTestClient.get().uri("/orders/300")
                .exchange().expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .xpath("//h2").isEqualTo("Order #300")
                .xpath("//h3").isEqualTo("Total: 12.0 $");
    }

    @Test
    public void buyOrderTest() {
        webTestClient.post().uri("/items/300")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("action", "plus"))
                .exchange().expectStatus().is3xxRedirection();

        webTestClient.post().uri("/cart/buy").exchange().expectStatus().is3xxRedirection();

        webTestClient.get().uri("/orders/1")
                .exchange().expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .xpath("//h2").isEqualTo("Order #1")
                .xpath("//h3").isEqualTo("Total: 1200.0 $")
                .xpath("//h1").isEqualTo("Your order is placed!");

    }
}
