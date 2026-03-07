package com.br.vidya.controller;

import com.br.vidya.integration.sankhya.service.SankhyaAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/sankhya/session")
@RequiredArgsConstructor
public class SankhyaSessionController {

    private final SankhyaAuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh() {
        authService.refreshSession();
        return ResponseEntity.noContent().build();
    }
}
