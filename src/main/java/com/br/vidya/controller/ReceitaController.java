package com.br.vidya.controller;

import com.br.vidya.integration.receita.dto.ReceitaResponse;
import com.br.vidya.integration.receita.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cnpj")
@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaService receitaService;

    @GetMapping()
    public ResponseEntity<ReceitaResponse> findByCnpj(@RequestParam String cnpj) {
        return ResponseEntity.ok(receitaService.findByCnpj(cnpj));
    }
}
