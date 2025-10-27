package com.mivivienda.simulador.simuladordecredito.auth.interfaces.rest;

import com.mivivienda.simulador.simuladordecredito.auth.application.internal.commandservices.AuthService;
import com.mivivienda.simulador.simuladordecredito.auth.dto.AuthResponse;
import com.mivivienda.simulador.simuladordecredito.auth.dto.LoginRequest;
import com.mivivienda.simulador.simuladordecredito.auth.dto.RefreshTokenRequest;
import com.mivivienda.simulador.simuladordecredito.auth.dto.RegisterRequest;
import com.mivivienda.simulador.simuladordecredito.auth.dto.RegisterResponse;
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

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
