package com.invoiceapp.auth;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        String token = authService.authenticate(request.username(), request.password());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public record LoginRequest(String username, String password) {}
    public record JwtResponse(String token) {}
}
