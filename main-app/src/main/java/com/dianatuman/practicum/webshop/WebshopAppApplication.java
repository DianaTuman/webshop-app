package com.dianatuman.practicum.webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WebshopAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebshopAppApplication.class, args);
    }

}
