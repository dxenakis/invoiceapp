package com.invoiceapp.auth;

import com.invoiceapp.aade.AadeClient;
import com.invoiceapp.aade.AadeCompanyDto;
import com.invoiceapp.auth.dto.*;
import com.invoiceapp.company.CompanyService;
import com.invoiceapp.company.dto.CompanyResponse;
import com.invoiceapp.exception.UserAlreadyExistsException;
import com.invoiceapp.securityconfig.JwtTokenProvider;
import com.invoiceapp.user.Role;
import com.invoiceapp.user.User;
import com.invoiceapp.user.UserRepository;
import com.invoiceapp.user.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;

@Service
public class AuthService {

    private final UserRepository users;
    private final CompanyService companies;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwt;
    private final AadeClient aadeClient; // <-- απλός client προς ΑΑΔΕ

    public AuthService(UserRepository users,
                       CompanyService companies,
                       PasswordEncoder encoder,
                       JwtTokenProvider jwt,
                       AadeClient aadeClient) {
        this.users = users;
        this.companies = companies;
        this.encoder = encoder;
        this.jwt = jwt;
        this.aadeClient = aadeClient;
    }

    public AuthResponse login(LoginRequest req){

        // 1. Βρες τον χρήστη στη βάση
        User user = users.findByUsername(req.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        // 2. Έλεγξε το password με τον encoder
        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // 3. Αν είναι σωστό, βγάλε JWT
        String token = jwt.generateToken(user);
        return new AuthResponse(token);

    }

    @Transactional
    public AuthResponse registerWithGov(RegisterGovRequest req) {
        validateUserUniqueness(req.username(), req.email());

        // 1) Φέρε στοιχεία εταιρείας από ΑΑΔΕ με το access token που έφερε το front
        AadeCompanyDto info = aadeClient.fetchCompany(req.govAuthCode());

        // 2) Δημιούργησε/βρες εταιρεία με βάση το AFM
        CompanyResponse company = companies.createOrGetByAfm(
                info.afm(), info.companyName(),
                info.addressLine(), info.city(), info.postalCode(),
                info.countryCode(), info.companyEmail(), info.phone()
        );

        // 3) Δημιούργησε χρήστη (verifiedByGov = true)
        String token = finalizeRegistration(req.username(), req.password(), req.email(), company.id(), true);
        return new AuthResponse(token);
    }

    @Transactional
    public AuthResponse registerManual(RegisterManualRequest req) {
        validateUserUniqueness(req.username(), req.email());

        // Δημιουργία/εύρεση εταιρείας από τα δηλωθέντα στοιχεία (AFM)
        CompanyResponse company = companies.createOrGetByAfm(
                req.afm(), req.companyName(),
                req.addressLine(), req.city(), req.postalCode(),
                req.countryCode(), req.companyEmail(), req.phone()
        );

        String token = finalizeRegistration(req.username(), req.password(), req.email(), company.id(), false);
        return new AuthResponse(token);
    }

    private void validateUserUniqueness(String username, String email) {
        if (users.existsByUsername(username)) throw new UserAlreadyExistsException("Username already exists");
        if (email != null && !email.isBlank() && users.existsByEmail(email)) throw new IllegalArgumentException("Email already exists");
    }

    private String finalizeRegistration(String username, String rawPassword, String email,
                                        Long companyId, boolean verifiedByGov) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setEmail(email);
        u.setCompanyId(companyId);

        long count = users.countByCompanyId(companyId);
        if (count == 0) {
            u.setRoles(EnumSet.of(Role.ADMIN));
            u.setStatus(UserStatus.ACTIVE);
        } else {
            u.setRoles(EnumSet.of(Role.VIEWER));
            u.setStatus(verifiedByGov ? UserStatus.ACTIVE : UserStatus.PENDING_APPROVAL);
        }

        users.save(u);
        return jwt.generateToken(u);
    }
}
