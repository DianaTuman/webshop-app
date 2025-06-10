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
public class ItemServiceTest extends BaseServiceTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @Container
    @ServiceConnection
    static final RedisContainer redisContainer =
            new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"));

    @Test
    public void getItemsTest() {
        webTestClient.get().uri("/items").exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .xpath("//table").nodeCount(8);
    }

    @Test
    public void getItemTest() {
        webTestClient.get().uri("/items/100").exchange()
                .expectStatus()
                .is2xxSuccessful();
    }

    @Test
    public void itemCartTest() {
        String itemUri = "/items/100";
        String cartItemsUri = "/cart/items";
        BodyInserters.MultipartInserter plus = BodyInserters.fromMultipartData("action", "plus");
        BodyInserters.MultipartInserter minus = BodyInserters.fromMultipartData("action", "minus");
        BodyInserters.MultipartInserter delete = BodyInserters.fromMultipartData("action", "delete");

        webTestClient.post().uri(itemUri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(plus)
                .exchange().expectStatus().is3xxRedirection();
        webTestClient.get().uri(cartItemsUri).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .xpath("//form/span").isEqualTo("1");

        webTestClient.post().uri(itemUri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(delete)
                .exchange().expectStatus().is3xxRedirection();
        webTestClient.get().uri(cartItemsUri).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .xpath("//form/span").doesNotExist();

        webTestClient.post().uri(itemUri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(plus)
                .exchange().expectStatus().is3xxRedirection();
        webTestClient.post().uri(itemUri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(plus)
                .exchange().expectStatus().is3xxRedirection();
        webTestClient.get().uri(cartItemsUri).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .xpath("//form/span").isEqualTo("2");


        webTestClient.post().uri(itemUri)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(minus)
                .exchange().expectStatus().is3xxRedirection();
        webTestClient.get().uri(cartItemsUri).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .xpath("//form/span").isEqualTo("1");
    }
}
