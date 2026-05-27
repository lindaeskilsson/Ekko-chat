package com.ekko.auth.web;

import com.ekko.auth.config.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final RestClient restClient;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        var user = restClient.get()
                .uri("http://localhost:8082/users/username/" + request.getUsername())
                .retrieve()
                .toEntity(UserDto.class)
                .getBody();

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtConfig.generateToken(user.getId(), user.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}