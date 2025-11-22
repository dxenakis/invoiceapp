package com.invoiceapp.auth;

import com.invoiceapp.auth.dto.*;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req, HttpServletResponse res) {
        return ResponseEntity.ok(auth.login(req, res));
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Principal principal) {
        String username = principal.getName();  // έρχεται από το access token
        return ResponseEntity.ok(auth.me(username));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerManual(@Valid @RequestBody RegisterManualRequest req,
                                                       HttpServletResponse res) {
        return ResponseEntity.ok(auth.register(req, res));
    }

    @PostMapping("/register/gov")
    public ResponseEntity<AuthResponse> registerGov(@Valid @RequestBody RegisterGovRequest req,
                                                    HttpServletResponse res) {
        return ResponseEntity.ok(auth.registerWithGov(req, res));
    }

    @PostMapping("/switch-company")
    public ResponseEntity<AuthResponse> switchCompany(Principal principal,
                                                      @RequestBody SwitchCompanyRequest req) {
        String username = principal.getName();
        return ResponseEntity.ok(auth.switchCompany(username, req.companyId()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @CookieValue(name = "refresh", required = false) String refreshCookie,
            @RequestBody(required = false) RefreshRequest body) {
        return ResponseEntity.ok(auth.refresh(refreshCookie, body));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Principal principal, HttpServletResponse res) {
        if (principal != null) {
            auth.logout(principal.getName(), res);
        } else {
            CookieUtils.clearRefreshCookie(res);
        }
        return ResponseEntity.noContent().build();
    }
}
