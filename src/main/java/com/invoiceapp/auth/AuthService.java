package com.invoiceapp.auth;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.invoiceapp.securityconfig.JwtTokenProvider;
import com.invoiceapp.user.User;
import com.invoiceapp.user.UserRepository;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(AuthenticationManager authManager,
                       JwtTokenProvider tokenProvider,
                       UserRepository userRepository){
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    public String authenticate(String username, String password){
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tokenProvider.generateToken(user);
    }
}
