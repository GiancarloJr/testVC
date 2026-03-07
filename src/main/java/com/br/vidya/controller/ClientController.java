package com.br.vidya.controller;

import com.br.vidya.dto.request.ClientRequest;
import com.br.vidya.dto.response.ClientResponse;
import com.br.vidya.dto.response.PageResponse;
import com.br.vidya.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        return ResponseEntity.ok(clientService.create(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ClientResponse>> getClients(
            @RequestParam(required = false) String codParc,
            @RequestParam(required = false) String cgcCpf,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(clientService.getClients(codParc, cgcCpf, pageable));
    }
}
