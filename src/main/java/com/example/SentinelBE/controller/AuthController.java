package com.example.SentinelBE.controller;


import com.example.SentinelBE.authentication.converter.RegistrationUserDtoConverter;
import com.example.SentinelBE.authentication.dto.LoginRequest;
import com.example.SentinelBE.authentication.dto.PasswordResetDto;
import com.example.SentinelBE.authentication.dto.RegistrationUserDto;
import com.example.SentinelBE.security.auth.AuthenticationService;
import com.example.SentinelBE.security.recovery.RecoveryService;
import com.example.SentinelBE.security.registration.RegistrationService;
import com.example.SentinelBE.security.user.SecurityUser;
import com.example.SentinelBE.service.UserService;
import com.example.SentinelBE.utils.Result;
import com.example.SentinelBE.utils.converter.UserDtoConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base-url}/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RegistrationUserDtoConverter registrationUserDtoConverter;
    private final RegistrationService registrationService;
    private final RecoveryService recoveryService;
    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    @Operation(
            description = "Login endpoint.",
            summary = "Login endpoint. Generates the JWT needed for further operations."
    )
    @PostMapping("/login")
    @Transactional
    public Result<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        var authResult = this.authenticationService.authenticate(loginRequest);
        String accessToken = authResult.getAccessToken();
        String refreshToken = authResult.getRefreshToken();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // Set false for dev environments without HTTPS
                .path("/") // restrict to refresh endpoint path or root "/"
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Strict")
                .build();

        response.setHeader("Set-Cookie", refreshCookie.toString());

        return new Result<>(true, HttpStatus.OK.value(), "Login successful", accessToken);
    }

    @Operation(
            description = "REGISTER",
            summary = "Registration endpoint. After registering it sends an email with confirmation token."
    )
    @PostMapping("/register")
    public Result<?> register(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
        try {
            this.registrationService.addUser(this.registrationUserDtoConverter.createFromDto(registrationUserDto));
        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

//        this.registrationService.addUser(this.registrationUserDtoConverter.createFromDto(registrationUserDto));
        return new Result<>(true, HttpStatus.CREATED.value(), "User created successfully", null);
    }
    @Operation(
            description = "ENABLE USER",
            summary = "To enable the user the token received in the email must be passed to the endpoint."
    )
    @PutMapping("/enable/{token}")
    public Result<?> enableUser(@PathVariable String token) {
        this.registrationService.enableUser(token);
        return new Result<>(true, HttpStatus.OK.value(), "User enabled successfully.", null);
    }

    @Operation(
            description = "RESET PASSWORD REQUEST",
            summary = "Forgot password. It will send an email with a token/link for password reset."
    )
    @PostMapping("/reset-password")
    public Result<?> resetPasswordRequest(@RequestBody String email) {
        this.recoveryService.recoveryRequest(email);
        return new Result<>(true, HttpStatus.OK.value(), "Email with token sent. Please check your email.", null);
    }
    @Operation(
            description = "RESET PASSWORD",
            summary = "Token from the email must be passed as a path variable to reset the password. Random password is generated"
    )
    @PostMapping("/reset-password-email/{token}")
    public Result<?> sendPassword(@PathVariable String token) {
        this.recoveryService.resetPassword(token);
        return new Result<>(true, HttpStatus.OK.value(), "Password sent to email.", null);
    }

    @Operation(
            description = "RESET PASSWORD WITH LINK",
            summary = "Input new password along with the token provided in email."
    )
    @PutMapping("/reset-password")
    public Result<?> resetPassword(@RequestBody @Valid PasswordResetDto passwordResetDto) {
        this.recoveryService.resetPassword(passwordResetDto);
        return new Result<>(true, HttpStatus.OK.value(), "Password reset successfully", null);
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.setHeader("Set-Cookie", deleteCookie.toString());

        return new Result<>(true, HttpStatus.OK.value(), "Logged out successfully", null);
    }

    @GetMapping("/refresh-token")
    public Result<String> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return new Result<>(false, HttpStatus.UNAUTHORIZED.value(), "Refresh token missing", null);
        }

        try {
            Jwt decodedJwt = authenticationService.decodeToken(refreshToken);

            // You may want to check expiration, issuer, etc. here explicitly
            String username = decodedJwt.getSubject();
            // load user or build Authentication object
            String scope = decodedJwt.getClaimAsString("scope");
            var authorities = Stream.of(scope.split(" "))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            // Build Authentication object (you can create this helper method in JwtService if you want)
            SecurityUser securityUser = authenticationService.loadSecurityUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    securityUser,
                    null,
                    authorities
            );

            // Generate a new access token using JwtService
            String newAccessToken = authenticationService.generateToken(authentication);

            return new Result<>(true, HttpStatus.OK.value(), "Access token refreshed", newAccessToken);
        } catch (JwtException e) {
            return new Result<>(false, HttpStatus.UNAUTHORIZED.value(), "Invalid refresh token", null);
        }
    }




}
