package com.invoiceapp.auth;

import com.invoiceapp.access.UserCompanyAccess;
import com.invoiceapp.access.UserCompanyAccessService;
import com.invoiceapp.auth.dto.*;
import com.invoiceapp.exception.UserAlreadyExistsException;
import com.invoiceapp.securityconfig.JwtTokenProvider;
import com.invoiceapp.user.Role;
import com.invoiceapp.user.User;
import com.invoiceapp.user.UserRepository;
import com.invoiceapp.user.UserStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwt;
    private final UserCompanyAccessService access;

    public AuthService(UserRepository users,
                       PasswordEncoder encoder,
                       AuthenticationManager authManager,
                       JwtTokenProvider jwt,
                       UserCompanyAccessService access) {
        this.users = users;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwt = jwt;
        this.access = access;
    }

    // 🔑 LOGIN
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        User user = users.findByUsername(req.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        List<UserCompanyAccess> companies = access.getUserCompanies(user.getId());

        // Αν έχει ακριβώς μία εταιρεία, βγάλε token με ενεργό context (ευκολία UX)
        if (companies.size() == 1) {
            UserCompanyAccess uca = companies.get(0);
            String token = jwt.generateToken(user.getUsername(), uca.getRole(), uca.getCompanyId());
            return new LoginResponse(
                    token,
                    uca.getCompanyId(),
                    companies.stream()
                            .map(c -> new LoginResponse.CompanyAccessItem(c.getCompanyId(), c.getRole(), null))
                            .toList()
            );
        }

        // Διαφορετικά, token χωρίς active company — το UI θα κάνει switch
        String token = jwt.generateToken(user.getUsername());
        return new LoginResponse(
                token,
                null,
                companies.stream()
                        .map(c -> new LoginResponse.CompanyAccessItem(c.getCompanyId(), c.getRole(), null))
                        .toList()
        );
    }

    // 🧾 REGISTER (manual) — ΜΟΝΟ User
    @Transactional
    public AuthResponse registerManual(RegisterManualRequest req) {
        if (users.existsByUsername(req.username()))
            throw new UserAlreadyExistsException("Username already exists");
        if (users.existsByEmail(req.email()))
            throw new UserAlreadyExistsException("Email already exists");

        User u = new User();
        u.setUsername(req.username().trim());
        u.setPassword(encoder.encode(req.password()));
        u.setEmail(req.email().trim());
        u.setStatus(UserStatus.ACTIVE);
        users.save(u);

        // Token ΧΩΡΙΣ active company
        String token = jwt.generateToken(u.getUsername());
        return new AuthResponse(token);
    }

    // 🧾 REGISTER (gov) — επίσης ΜΟΝΟ User
    @Transactional
    public AuthResponse registerWithGov(RegisterGovRequest req) {
        if (users.existsByUsername(req.username()))
            throw new UserAlreadyExistsException("Username already exists");
        if (users.existsByEmail(req.email()))
            throw new UserAlreadyExistsException("Email already exists");

        // Εδώ θα έμπαινε ο gov έλεγχος/flow — προς το παρόν δημιουργούμε χρήστη
        User u = new User();
        u.setUsername(req.username().trim());
        u.setPassword(encoder.encode(req.password()));
        u.setEmail(req.email().trim());
        u.setStatus(UserStatus.ACTIVE);
        users.save(u);

        String token = jwt.generateToken(u.getUsername());
        return new AuthResponse(token);
    }

    // 🔁 Switch Company
    @Transactional(readOnly = true)
    public String switchCompany(String username, Long companyId) {
        User user = users.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Role role = access.roleFor(user.getId(), companyId)
                .orElseThrow(() -> new IllegalArgumentException("User has no access to the requested company"));

        return jwt.generateToken(user.getUsername(), role, companyId);
    }
}
