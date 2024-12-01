package com.debadatta.TrimTime.config;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;

    @PostMapping("/generate")
    public String generateToken(@RequestParam("username") String username) {
        return jwtService.generateToken(username);
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestParam("token") String token) {
        if (jwtService.isTokenExpired(token)) {
            throw new RuntimeException("Token is expired. Please log in again.");
        }
        String username = jwtService.extractUsername(token);
        return jwtService.generateToken(username);
    }
}
