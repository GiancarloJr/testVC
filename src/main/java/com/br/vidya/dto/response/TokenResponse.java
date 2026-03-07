package com.br.vidya.dto.response;

public record TokenResponse(
        String accessToken,
        Long expiresIn,
        String refreshToken
) {
}
