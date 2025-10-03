
package com.invoiceapp.auth;

import com.invoiceapp.auth.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register/gov")
    public ResponseEntity<AuthResponse> registerWithGov(@Valid @RequestBody RegisterGovRequest req) {
        return ResponseEntity.ok(auth.registerWithGov(req));
    }

    @PostMapping("/register/manual")
    public ResponseEntity<AuthResponse> registerManual(@Valid @RequestBody RegisterManualRequest req) {
        return ResponseEntity.ok(auth.registerManual(req));
    }
}
