package com.dianatuman.practicum.webshop.configuration;

import com.dianatuman.practicum.webshop.service.WebUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
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
                    //TODO show message
                    return Mono.just(new UsernamePasswordAuthenticationToken(username, authentication.getCredentials()));
                });
    }
}
