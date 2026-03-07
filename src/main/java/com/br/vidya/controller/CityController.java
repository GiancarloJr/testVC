package com.br.vidya.controller;

import com.br.vidya.dto.response.CityResponse;
import com.br.vidya.dto.response.PageResponse;
import com.br.vidya.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping
    public ResponseEntity<PageResponse<CityResponse>> listCities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) List<String> codCid,
            @PageableDefault(size = 50) Pageable pageable) {

        String[] codCidArray = (codCid == null || codCid.isEmpty()) ? null : codCid.toArray(String[]::new);
        return ResponseEntity.ok(cityService.getCities(name, pageable, codCidArray));
    }
}
