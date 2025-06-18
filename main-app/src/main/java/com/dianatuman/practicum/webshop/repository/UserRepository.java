package com.dianatuman.practicum.webshop.repository;

import com.dianatuman.practicum.webshop.entity.WebUser;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<WebUser, Long> {

    Mono<WebUser> findByUsername(String username);

}
