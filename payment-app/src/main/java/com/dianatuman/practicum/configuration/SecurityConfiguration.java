package com.dianatuman.practicum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity security) {
        return security
                .authorizeExchange(requests -> requests
                        .anyExchange().authenticated())
                .oauth2ResourceServer(serverSpec -> serverSpec
                        .jwt(Customizer.withDefaults()))
                .build();
    }
}
