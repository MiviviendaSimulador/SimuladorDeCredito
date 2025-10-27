package com.mivivienda.simulador.simuladordecredito.auth.application.internal.commandservices;

import com.mivivienda.simulador.simuladordecredito.auth.application.internal.outboundservices.JwtService;
import com.mivivienda.simulador.simuladordecredito.auth.domain.model.RefreshToken;
import com.mivivienda.simulador.simuladordecredito.auth.domain.model.Role;
import com.mivivienda.simulador.simuladordecredito.auth.domain.model.User;
import com.mivivienda.simulador.simuladordecredito.auth.domain.persistence.RefreshTokenRepository;
import com.mivivienda.simulador.simuladordecredito.auth.domain.persistence.UserRepository;
import com.mivivienda.simulador.simuladordecredito.auth.dto.AuthResponse;
import com.mivivienda.simulador.simuladordecredito.auth.dto.LoginRequest;
import com.mivivienda.simulador.simuladordecredito.auth.dto.RefreshTokenRequest;
import com.mivivienda.simulador.simuladordecredito.auth.dto.RegisterRequest;
import com.mivivienda.simulador.simuladordecredito.auth.dto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        var accessToken = jwtService.generateToken(user);
        var refreshTokenString = jwtService.generateRefreshToken(user);

        var refreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(refreshTokenString)
                .expiresAt(LocalDateTime.now().plusMillis(jwtService.getRefreshExpiration()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenString)
                .tokenType("Bearer")
                .expiresIn(jwtService.getJwtExpiration())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        var refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token has expired");
        }

        var user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!jwtService.isTokenValid(request.getRefreshToken(), user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        var accessToken = jwtService.generateToken(user);
        var newRefreshTokenString = jwtService.generateRefreshToken(user);

        var newRefreshToken = RefreshToken.builder()
                .userId(user.getId())
                .token(newRefreshTokenString)
                .expiresAt(LocalDateTime.now().plusMillis(jwtService.getRefreshExpiration()))
                .revoked(false)
                .build();
        refreshTokenRepository.save(newRefreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshTokenString)
                .tokenType("Bearer")
                .expiresIn(jwtService.getJwtExpiration())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
