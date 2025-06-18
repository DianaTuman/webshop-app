package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.entity.WebUser;
import com.dianatuman.practicum.webshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WebUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username).map(WebUser::new);
    }
}
