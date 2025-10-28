package com.invoiceapp.auth;

import com.invoiceapp.access.UserCompanyAccess;
import com.invoiceapp.auth.dto.*;
import com.invoiceapp.company.CompanyRepository;
import com.invoiceapp.securityconfig.JwtTokenProvider;
import com.invoiceapp.user.User;
import com.invoiceapp.user.UserRepository;
import com.invoiceapp.user.UserStatus;
import com.invoiceapp.access.UserCompanyAccessService;
import com.invoiceapp.user.Role;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final CompanyRepository companyRepo;
    private final UserCompanyAccessService accessService;
    private final JwtTokenProvider jwt;

    public AuthService(AuthenticationManager authManager,
                       PasswordEncoder passwordEncoder,
                       UserRepository userRepo,
                       CompanyRepository companyRepo,
                       UserCompanyAccessService accessService,
                       JwtTokenProvider jwt) {
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
        this.accessService = accessService;
        this.jwt = jwt;
    }

    /* ===== LOGIN: εκδίδει PRE-TENANT access (χωρίς act_cid & χωρίς role) + refresh cookie ===== */
    @Transactional
    public LoginResponse login(LoginRequest req, HttpServletResponse res) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid credentials");
        }

        User user = userRepo.findByUsername(req.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BadCredentialsException("Inactive user");
        }
        List<UserCompanyAccess> companies = accessService.getUserCompanies(user.getId());

        List<LoginResponse.CompanyAccessItem>  userCompanies =  companies.stream()
                            .map(c -> new LoginResponse.CompanyAccessItem(c.getCompanyId(), c.getRole(), null))
                            .toList();


        // PRE-TENANT access: actCid = null, role = null (δεν γράφεται claim)
        String accessToken = jwt.generateAccessToken(user.getUsername(), null, null);
        String refreshToken = jwt.generateRefreshToken(user.getUsername(), user.getRefreshVersion());

        CookieUtils.setRefreshCookie(res, refreshToken);
        return new LoginResponse(accessToken,null,userCompanies);
    }

    /* ===== REGISTER (manual): δημιουργεί χρήστη + auto-login (pre-tenant) ===== */
    @Transactional
    public AuthResponse registerManual(RegisterManualRequest req, HttpServletResponse res) {
        userRepo.findByUsername(req.username()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already exists");
        });

        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setEmail(req.email());
        user.setStatus(UserStatus.ACTIVE);
        user.setRefreshVersion(1);
        userRepo.save(user);

        // Auto-login (pre-tenant)
        String accessToken = jwt.generateAccessToken(user.getUsername(), null, null);
        String refreshToken = jwt.generateRefreshToken(user.getUsername(), user.getRefreshVersion());
        CookieUtils.setRefreshCookie(res, refreshToken);

        return new AuthResponse(accessToken);
    }

    /* ===== REGISTER (gov): όπως και το manual, χωρίς role στο User ===== */
    @Transactional
    public AuthResponse registerWithGov(RegisterGovRequest req, HttpServletResponse res) {
        userRepo.findByUsername(req.username()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already exists");
        });

        // TODO: εδώ μπαίνει η πραγματική gov ταυτοποίηση
        User user = new User();
        user.setUsername(req.username());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRefreshVersion(1);
        userRepo.save(user);

        String accessToken = jwt.generateAccessToken(user.getUsername(), null, null); // pre-tenant
        String refreshToken = jwt.generateRefreshToken(user.getUsername(), user.getRefreshVersion());
        CookieUtils.setRefreshCookie(res, refreshToken);

        return new AuthResponse(accessToken);
    }

    /* ===== SWITCH COMPANY: εκδίδει TENANT access (με act_cid & role από UserCompanyAccess) ===== */
    @Transactional(readOnly = true)
    public AuthResponse switchCompany(String username, Long companyId) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (companyId != null && !companyRepo.existsById(companyId)) {
            throw new IllegalArgumentException("Company not found: " + companyId);
        }
        if (companyId != null) {
            // validate access & πάρ’ το role από το mapping
            Role roleForCompany = accessService.roleFor(user.getId(), companyId)
                    .orElseThrow(() -> new AccessDeniedException("No access to company " + companyId));

            String accessToken = jwt.generateAccessToken(user.getUsername(), roleForCompany, companyId);
            return new AuthResponse(accessToken);
        }

        // Αν για κάποιο λόγο ζητηθεί "null" company, επιστρέφουμε pre-tenant access
        String accessToken = jwt.generateAccessToken(user.getUsername(), null, null);
        return new AuthResponse(accessToken);
    }

    /* ===== REFRESH: ελέγχει refresh cookie (ver) και εκδίδει νέο access =====
       - Αν actCid != null: role από UserCompanyAccess & act_cid στο token
       - Αν actCid == null: pre-tenant access (χωρίς role/act_cid claims)
    */
    @Transactional(readOnly = true)
    public RefreshResponse refresh(String refreshCookie, RefreshRequest body) {
        if (refreshCookie == null || refreshCookie.isBlank()) {
            throw new BadCredentialsException("Missing refresh cookie");
        }
        if (!jwt.validateRefresh(refreshCookie)) {
            throw new BadCredentialsException("Invalid refresh");
        }

        String username = jwt.getUsername(refreshCookie)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh"));
        int ver = jwt.getRefreshVersion(refreshCookie)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh"));

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        if (!Integer.valueOf(ver).equals(user.getRefreshVersion())) {
            throw new BadCredentialsException("Refresh revoked");
        }

        Long actCid = Optional.ofNullable(body).map(RefreshRequest::actCid).orElse(null);
        if (actCid != null) {
            Role roleForCompany = accessService.roleFor(user.getId(), actCid)
                    .orElseThrow(() -> new AccessDeniedException("No access to company " + actCid));
            String newAccess = jwt.generateAccessToken(user.getUsername(), roleForCompany, actCid);
            return new RefreshResponse(newAccess);
        } else {
            // pre-tenant access
            String newAccess = jwt.generateAccessToken(user.getUsername(), null, null);
            return new RefreshResponse(newAccess);
        }
    }

    /* ===== LOGOUT: revoke-all refresh (version++) + clear cookie ===== */
    @Transactional
    public void logout(String username, HttpServletResponse res) {
        userRepo.findByUsername(username).ifPresent(u -> {
            u.incrementRefreshVersion();
            userRepo.save(u);
        });
        CookieUtils.clearRefreshCookie(res);
    }
}
