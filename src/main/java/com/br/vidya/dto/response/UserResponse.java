package com.br.vidya.dto.response;

import com.br.vidya.model.enums.Role;

import java.util.Set;

public record UserResponse(
        Long id,
        String name,
        String email,
        Set<Role> roles
) {}
