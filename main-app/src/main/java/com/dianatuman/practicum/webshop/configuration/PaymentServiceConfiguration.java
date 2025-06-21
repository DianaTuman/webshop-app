package com.dianatuman.practicum.webshop.configuration;

import com.dianatuman.practicum.webshop.service.OAuth2TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class PaymentServiceConfiguration {

    @Bean
    public WebClient paymentWebClient(OAuth2TokenService oAuth2TokenService) {
        String paymentService = System.getenv("PAYMENT_SERVICE");
        if (paymentService == null || paymentService.isEmpty()) {
            paymentService = "http://localhost:8081";
        }

        return WebClient.builder()
                .baseUrl(paymentService)
                .filter((request, next) -> {
                    log.info("Payment WebClient filter: Processing request to {}", request.url());
                    return oAuth2TokenService.getAccessToken()
                            .doOnNext(token -> log.info("Payment WebClient filter: Got token: {}",
                                    token.substring(0, Math.min(20, token.length())) + "..."))
                            .flatMap(token -> {
                                HttpHeaders newHeaders = new HttpHeaders();
                                newHeaders.putAll(request.headers());
                                newHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                                ClientRequest newRequest = ClientRequest.from(request)
                                        .headers(headers -> headers.putAll(newHeaders))
                                        .build();

                                return next.exchange(newRequest);
                            })
                            .doOnError(error -> log.error("Payment WebClient filter: Error getting token or making request", error))
                            .onErrorResume(error -> {
                                log.error("Payment WebClient filter: Resuming with error", error);
                                return next.exchange(request);
                            });
                })
                .build();
    }
}
