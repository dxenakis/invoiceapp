package com.invoiceapp.auth;

import com.invoiceapp.aade.AadeClient;
import com.invoiceapp.aade.AadeCompanyDto;
import com.invoiceapp.access.UserCompanyAccess;
import com.invoiceapp.access.UserCompanyAccessService;
import com.invoiceapp.auth.dto.*;
import com.invoiceapp.company.CompanyService;
import com.invoiceapp.company.dto.CompanyResponse;
import com.invoiceapp.exception.UserAlreadyExistsException;
import com.invoiceapp.securityconfig.JwtTokenProvider;
import com.invoiceapp.user.Role;
import com.invoiceapp.user.User;
import com.invoiceapp.user.UserRepository;
import com.invoiceapp.user.UserStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository users;
    private final CompanyService companies;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwt;
    private final AadeClient aadeClient;
    private final UserCompanyAccessService userCompanyAccessService;

    public AuthService(UserRepository users,
                       CompanyService companies,
                       PasswordEncoder encoder,
                       JwtTokenProvider jwt,
                       AadeClient aadeClient,
                       UserCompanyAccessService userCompanyAccessService) {
        this.users = users;
        this.companies = companies;
        this.encoder = encoder;
        this.jwt = jwt;
        this.aadeClient = aadeClient;
        this.userCompanyAccessService = userCompanyAccessService;
    }

    // 🔑 LOGIN
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        User user = users.findByUsername(req.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        List<UserCompanyAccess> accessList = userCompanyAccessService.getUserCompanies(user.getId());
        if (accessList.isEmpty()) {
            throw new IllegalStateException("User has no company access");
        }

        Long activeCompanyId = accessList.get(0).getCompanyId();
        Role role = accessList.get(0).getRole(); // 🔑 παίρνουμε τον ρόλο από το access

        String token = jwt.generateToken(user.getUsername(), role, activeCompanyId);

        Map<Long, String> namesById = companies.getCompanyNamesByIds(
                accessList.stream().map(UserCompanyAccess::getCompanyId).toList()
        );

        List<LoginResponse.CompanyAccessItem> companiesList = accessList.stream()
                .map(a -> new LoginResponse.CompanyAccessItem(
                        a.getCompanyId(),
                        a.getRole(),
                        namesById.get(a.getCompanyId())
                ))
                .toList();

        return new LoginResponse(token, activeCompanyId, companiesList);
    }

    // REGISTER (Gov)
    @Transactional
    public AuthResponse registerWithGov(RegisterGovRequest req) {
        validateUserUniqueness(req.username(), req.email());

        AadeCompanyDto info = aadeClient.fetchCompany(req.govAuthCode());

        CompanyResponse company = companies.createOrGetByAfm(
                info.afm(), info.companyName(),
                info.addressLine(), info.city(), info.postalCode(),
                info.countryCode(), info.companyEmail(), info.phone()
        );

        String token = finalizeRegistration(req.username(), req.password(), req.email(), company.id(), true);
        return new AuthResponse(token);
    }

    //REGISTER (Manual)
    @Transactional
    public AuthResponse registerManual(RegisterManualRequest req) {
        validateUserUniqueness(req.username(), req.email());

        CompanyResponse company = companies.createOrGetByAfm(
                req.afm(), req.companyName(),
                req.addressLine(), req.city(), req.postalCode(),
                req.countryCode(), req.companyEmail(), req.phone()
        );

        String token = finalizeRegistration(req.username(), req.password(), req.email(), company.id(), false);
        return new AuthResponse(token);
    }

    private void validateUserUniqueness(String username, String email) {
        if (users.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (email != null && !email.isBlank() && users.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists");
        }
    }

    // 🔑 Registration logic
    private String finalizeRegistration(String username, String rawPassword, String email,
                                        Long companyId, boolean verifiedByGov) {
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setEmail(email);

        long count = userCompanyAccessService.countUsersForCompany(companyId);

        Role assignedRole;
        if (count == 0) {
            assignedRole = Role.COMPANY_ADMIN;
            u.setStatus(UserStatus.ACTIVE);
        } else {
            assignedRole = Role.VIEWER;
            u.setStatus(verifiedByGov ? UserStatus.ACTIVE : UserStatus.PENDING_APPROVAL);
        }

        users.save(u);

        userCompanyAccessService.grantAccess(u.getId(), companyId, assignedRole);

        // το JWT πλέον χτίζεται με username + role + act_cid
        return jwt.generateToken(u.getUsername(), assignedRole, companyId);
    }

    // 🔑 Switch Company
    public String switchCompany(String username, Long companyId) {
        User user = users.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Role role = userCompanyAccessService.roleFor(user.getId(), companyId)
                .orElseThrow(() -> new IllegalArgumentException("User has no access to the requested company"));

        return jwt.generateToken(user.getUsername(), role, companyId);
    }
}
