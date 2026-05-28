package com.ekko.bff.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequiredArgsConstructor
public class BffController {

    private final RestClient restClient;

    @Value("${services.user}")
    private String userService;

    @Value("${services.auth}")
    private String authService;

    @Value("${services.message}")
    private String messageService;

    @PostMapping("/users/register")
    public ResponseEntity<String> register(@RequestBody Object body) {
        return restClient.post()
                .uri(userService + "/users")
                .body(body)
                .retrieve()
                .toEntity(String.class);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody Object body) {
        return restClient.post()
                .uri(authService + "/auth/login")
                .body(body)
                .retrieve()
                .toEntity(String.class);
    }

    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestBody Object body,
                                              @RequestHeader("Authorization") String token) {
        return restClient.post()
                .uri(messageService + "/messages")
                .header("Authorization", token)
                .body(body)
                .retrieve()
                .toEntity(String.class);
    }

    @GetMapping("/messages")
    public ResponseEntity<String> getMessages(@RequestHeader("Authorization") String token) {
        return restClient.get()
                .uri(messageService + "/messages")
                .header("Authorization", token)
                .retrieve()
                .toEntity(String.class);
    }
}