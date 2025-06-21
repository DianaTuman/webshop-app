package com.dianatuman.practicum.webshop.configuration;

import com.dianatuman.practicum.webshop.service.OAuth2TokenService;
import com.dianatuman.practicum.webshop.service.WebUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerHttpBasicAuthenticationConverter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfiguration {

    @Autowired
    private WebUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveAuthenticationManager authenticationManager) {
        AuthenticationWebFilter authFilter = new AuthenticationWebFilter(authenticationManager);
        authFilter.setServerAuthenticationConverter(new ServerHttpBasicAuthenticationConverter());

        return http
                .addFilterAt(authFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/items", "/items/**", "/images/**").permitAll()
                        .anyExchange().authenticated())
                .anonymous(anonymous -> anonymous.principal("guestUser"))
                .securityContextRepository(new WebSessionServerSecurityContextRepository())
                .formLogin(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }

    @Bean
    protected ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> userDetailsService.findByUsername(authentication.getPrincipal().toString())
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .flatMap(user -> {

                    final String username = authentication.getPrincipal().toString();
                    final CharSequence rawPassword = authentication.getCredentials().toString();

                    if (passwordEncoder().matches(rawPassword, user.getPassword())) {
                        return Mono.just(new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities()));
                    }
                    return Mono.just(new UsernamePasswordAuthenticationToken(username, authentication.getCredentials()));
                });
    }

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
                                // Get current user ID from security context
                                // Create a new request with the Authorization header and user context
                                HttpHeaders newHeaders = new HttpHeaders();
                                newHeaders.putAll(request.headers());
                                newHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

                                // Create a new request with the modified headers
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
