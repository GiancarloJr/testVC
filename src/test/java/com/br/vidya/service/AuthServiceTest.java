package com.br.vidya.service;

import com.br.vidya.dto.request.LoginRequest;
import com.br.vidya.dto.request.RegisterRequest;
import com.br.vidya.dto.response.TokenResponse;
import com.br.vidya.dto.response.UserResponse;
import com.br.vidya.exception.BusinessException;
import com.br.vidya.mapper.UserMapper;
import com.br.vidya.model.RefreshToken;
import com.br.vidya.model.User;
import com.br.vidya.repository.RefreshTokenRepository;
import com.br.vidya.repository.UserRepository;
import com.br.vidya.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        var request = mock(LoginRequest.class);
        when(request.email()).thenReturn("user@test.com");

        var user = mock(User.class);

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");
        when(jwtService.generateRefreshToken()).thenReturn("refresh-token");
        when(jwtService.getExpiration()).thenReturn(86400000L);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArgument(0));

        TokenResponse response = authService.login(request);

        assertThat(response.accessToken()).isEqualTo("jwt-token");
        assertThat(response.expiresIn()).isEqualTo(86400000L);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_shouldThrowBusinessException_whenUserNotFound() {
        var request = mock(LoginRequest.class);
        when(request.email()).thenReturn("user@test.com");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("User not found with email");
    }

    @Test
    void login_shouldThrowException_whenAuthenticationFails() {
        var request = mock(LoginRequest.class);
        var user = mock(User.class);

        when(request.email()).thenReturn("user@test.com");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void register_shouldReturnUserResponse_whenEmailIsNew() {
        var request = mock(RegisterRequest.class);
        var savedUser = mock(User.class);
        var expectedResponse = mock(UserResponse.class);

        when(request.password()).thenReturn("123456");
        when(expectedResponse.id()).thenReturn(1L);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(expectedResponse);

        UserResponse response = authService.register(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo(request.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowBusinessException_whenEmailAlreadyExists() {
        var request = mock(RegisterRequest.class);
        when(request.email()).thenReturn("test@test.com");

        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("E-mail already in use");
    }
}
