package com.dianatuman.practicum.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(BalanceController.class)
public class BalanceControllerTest {

    @Autowired
    protected WebTestClient webTestClient;

    @Test
    public void balanceTest() {
        webTestClient.get()
                .uri("/balance")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Double.class)
                .isEqualTo(100000.0);

        webTestClient.put()
                .uri("/balance")
                .bodyValue(100000)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        webTestClient.get()
                .uri("/balance")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Double.class)
                .isEqualTo(0.0);

        webTestClient.put()
                .uri("/balance")
                .bodyValue(-100)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

}
