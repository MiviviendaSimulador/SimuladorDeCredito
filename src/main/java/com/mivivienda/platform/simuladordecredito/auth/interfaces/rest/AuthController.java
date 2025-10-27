package com.mivivienda.platform.simuladordecredito.auth.interfaces.rest;

import com.mivivienda.platform.simuladordecredito.auth.application.internal.commandservices.AuthService;
import com.mivivienda.platform.simuladordecredito.auth.dto.AuthResponse;
import com.mivivienda.platform.simuladordecredito.auth.dto.LoginRequest;
import com.mivivienda.platform.simuladordecredito.auth.dto.RefreshTokenRequest;
import com.mivivienda.platform.simuladordecredito.auth.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user
     * @param request RegisterRequest containing username, email, and password
     * @return AuthResponse with access token and user details
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Login with existing credentials
     * @param request LoginRequest containing username and password
     * @return AuthResponse with access token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    /**
     * Refresh access token using refresh token
     * @param request RefreshTokenRequest containing the refresh token
     * @return AuthResponse with new access token and refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Token refresh failed: " + e.getMessage());
        }
    }
}
