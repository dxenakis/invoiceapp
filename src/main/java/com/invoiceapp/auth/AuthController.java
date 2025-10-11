package com.invoiceapp.auth;

import com.invoiceapp.auth.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(auth.login(req));
    }

    @PostMapping("/switch-company")
    public ResponseEntity<AuthResponse> switchCompany(@Valid @RequestBody SwitchCompanyRequest req,
                                                      Principal principal) {
        // basic guard για null/μη έγκυρο id -> 400
        if (req.companyId() == null || req.companyId() <= 0) {
            throw new IllegalArgumentException("Invalid companyId");
        }

        String username = principal.getName();
        String newToken = auth.switchCompany(username, req.companyId());
        return ResponseEntity.ok(new AuthResponse(newToken));
    }

    @PostMapping("/register/gov")
    public ResponseEntity<AuthResponse> registerWithGov(@Valid @RequestBody RegisterGovRequest req) {
        return ResponseEntity.ok(auth.registerWithGov(req));
    }

    @PostMapping("/register/manual")
    public ResponseEntity<AuthResponse> registerManual(@Valid @RequestBody RegisterManualRequest req) {
        return ResponseEntity.ok(auth.registerManual(req));
    }
}
