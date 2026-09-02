package com.marketplace.hola.admin;

import com.marketplace.hola.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminAuthController {

    private final JwtService jwtService;
    private static final String PLAIN_ADMIN_PASSWORD = "haola@#2050";

    public AdminAuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        String inputPassword = body.get("password") != null ? String.valueOf(body.get("password")).trim() : "";

        System.out.println(">>> RECEIVED PASSWORD: [" + inputPassword + "]");
        System.out.println(">>> EXPECTED PASSWORD: [" + PLAIN_ADMIN_PASSWORD + "]");

        if (!PLAIN_ADMIN_PASSWORD.equals(inputPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Incorrect password"));
        }

        String token = jwtService.generateToken("admin");
        return ResponseEntity.ok(Map.of("token", token));
    }
}