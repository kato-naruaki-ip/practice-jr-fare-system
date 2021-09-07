package com.myexample.practicespring.authorization;

import domain.ip.authorization.AuthRequest;
import domain.ip.authorization.AuthRequestRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("authorization")
public class AuthorizationController {

    private final AuthRequestRepository repository;
    private final AtomicLong counter = new AtomicLong(12345L);

    public AuthorizationController(AuthRequestRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public AuthRequest home() {
        return repository.first();
    }

    @GetMapping("/all")
    public List<AuthRequest> all() {
        return repository.all();
    }

    @GetMapping("/add")
    public AuthRequest add() {
        AuthRequest request = AuthRequest.builder()
                .id(counter.getAndIncrement())
                .isDeleted(true)
                .build();

        repository.add(request);

        return request;
    }
}
