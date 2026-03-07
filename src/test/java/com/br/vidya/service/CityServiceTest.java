package com.br.vidya.service;

import com.br.vidya.dto.response.CityResponse;
import com.br.vidya.dto.response.PageResponse;
import com.br.vidya.integration.sankhya.service.SankhyaCityService;
import com.br.vidya.mapper.CityMapper;
import com.br.vidya.model.City;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityMapper cityMapper;

    @Mock
    private SankhyaCityService sankhyaCityService;

    @InjectMocks
    private CityService cityService;

    @Test
    void getCities_shouldReturnPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        City city = mock(City.class);
        CityResponse cityResponse = mock(CityResponse.class);

        when(sankhyaCityService.sync(eq("São Paulo"), eq("0"))).thenReturn(List.of(city));
        when(cityMapper.toResponse(city)).thenReturn(cityResponse);

        PageResponse<CityResponse> result = cityService.getCities("São Paulo", pageable);

        assertThat(result).isNotNull();
        assertThat(result.data()).hasSize(1);
        verify(sankhyaCityService).sync(eq("São Paulo"), eq("0"));
    }

    @Test
    void getCities_shouldReturnEmptyPage_whenNoResults() {
        Pageable pageable = PageRequest.of(0, 10);

        when(sankhyaCityService.sync(isNull(), eq("0"))).thenReturn(List.of());

        PageResponse<CityResponse> result = cityService.getCities(null, pageable);

        assertThat(result).isNotNull();
        assertThat(result.data()).isEmpty();
    }
}
