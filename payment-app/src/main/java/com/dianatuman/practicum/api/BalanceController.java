package com.dianatuman.practicum.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
public class BalanceController implements DefaultApi {

    private double balance = 100000;

    @Override
    public Mono<ResponseEntity<Void>> balancePut(Mono<Double> body, ServerWebExchange exchange) {
        return body.map(value -> {
            if (Math.abs(value) > balance) {
                return ResponseEntity.badRequest().build();
            } else {
                balance = balance - value;
                return ResponseEntity.ok().build();
            }
        });
    }

    @Override
    public Mono<ResponseEntity<Double>> balanceGet(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok().body(balance));
    }
}
