package com.br.vidya.service;

import com.br.vidya.model.RefreshToken;
import com.br.vidya.model.User;
import com.br.vidya.model.enums.Role;
import com.br.vidya.dto.request.LoginRequest;
import com.br.vidya.dto.request.RegisterRequest;
import com.br.vidya.dto.response.TokenResponse;
import com.br.vidya.dto.response.UserResponse;
import com.br.vidya.exception.BusinessException;
import com.br.vidya.mapper.UserMapper;
import com.br.vidya.repository.RefreshTokenRepository;
import com.br.vidya.repository.UserRepository;
import com.br.vidya.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        log.info("Authenticating user with email: {}", request.email());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("User not found with email: " + request.email() + "."));

        refreshTokenRepository.revokeAllByUserId(user.getId());

        String accessToken = jwtService.generateToken(user);

        return new TokenResponse(accessToken, jwtService.getExpiration(), saveRefreshToken(user));
    }

    @Transactional
    public TokenResponse refresh(String refreshTokenValue) {
        log.info("Refreshing token");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new BusinessException("Refresh token invalid."));

        validateRefreshToken(refreshToken);

        User user = refreshToken.getUser();

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshTokenValue = saveRefreshToken(user);

        return new TokenResponse(newAccessToken, jwtService.getExpiration(), newRefreshTokenValue);
    }

    private String saveRefreshToken(User user) {

        String refreshTokenValue = jwtService.generateRefreshToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .user(user)
                .expiresAt(jwtService.refreshTokenExpiresAt())
                .build();

        refreshTokenRepository.save(refreshToken);

        return refreshTokenValue;
    }

    private void validateRefreshToken(RefreshToken refreshToken) {

        if (refreshToken.isRevoked()) {
            throw new BusinessException("Refresh token revoked.");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new BusinessException("Refresh token expired.");
        }
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        log.info("Registering user with email: {}", request.email());
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail already in use: " + request.email() + ".");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(Role.ROLE_USER))
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }
}
