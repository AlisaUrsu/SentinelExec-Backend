package com.example.SentinelBE.security.auth;


import com.example.SentinelBE.authentication.dto.LoginRequest;
import com.example.SentinelBE.repository.UserRepository;
import com.example.SentinelBE.security.jwt.JwtService;
import com.example.SentinelBE.security.user.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public AuthResult authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
            return new AuthResult(jwtService.generateToken(authentication), jwtService.generateRefreshToken(authentication));
        } catch (AuthenticationException ex) {
            LOGGER.error("Authentication failed with message: '{}' and stacktrace '{}' ", ex.getMessage(), ex.getStackTrace());
            throw new RuntimeException(ex);
        }
    }

    @Transactional
    public Jwt decodeToken(String token) {
        return  jwtService.decodeToken(token);
    }

    @Transactional
    public String generateToken(Authentication authentication) {
        return  jwtService.generateRefreshToken(authentication);
    }

    @Transactional
    public SecurityUser loadSecurityUserByUsername(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new SecurityUser(user);
    }




}
