package com.br.vidya.integration.sankhya.service;

import com.br.vidya.exception.ResourceNotFoundException;
import com.br.vidya.integration.sankhya.dto.LoadRecordsResponse;
import com.br.vidya.integration.sankhya.exceptions.SankhyaApiException;
import com.br.vidya.integration.sankhya.gateway.SankhyaGateway;
import com.br.vidya.mapper.CityMapper;
import com.br.vidya.model.City;
import com.br.vidya.repository.CityRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SankhyaCityServiceTest {

    @Mock
    private SankhyaGateway sankhyaGateway;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CityMapper cityMapper;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SankhyaCityService sankhyaCityService;

    private LoadRecordsResponse buildResponse(String status) {
        var entities = new LoadRecordsResponse.Entities(null, null, null, null, null, List.of());
        var responseBody = new LoadRecordsResponse.ResponseBody(entities);
        return new LoadRecordsResponse(null, status, null, null, null, responseBody);
    }

    @Test
    void sync_shouldReturnCities() {
        LoadRecordsResponse response = buildResponse("1");
        City city = mock(City.class);

        when(sankhyaGateway.post(any(), any(), eq(LoadRecordsResponse.class), any())).thenReturn(response);
        when(cityMapper.toCityList(any())).thenReturn(List.of(city));

        List<City> result = sankhyaCityService.sync("SP", "0");

        assertThat(result).hasSize(1);
        verify(cityRepository).saveOrUpdateAll(eq(List.of(city)), any());
        verify(entityManager).flush();
    }

    @Test
    void sync_shouldReturnEmptyList_whenNoCities() {
        LoadRecordsResponse response = buildResponse("1");

        when(sankhyaGateway.post(any(), any(), eq(LoadRecordsResponse.class), any())).thenReturn(response);
        when(cityMapper.toCityList(any())).thenReturn(List.of());

        List<City> result = sankhyaCityService.sync(null, "0");

        assertThat(result).isEmpty();
    }

    @Test
    void load_shouldThrowSankhyaApiException_whenStatusIsZero() {
        LoadRecordsResponse response = new LoadRecordsResponse(null, "0", null, null, "Error", null);

        when(sankhyaGateway.post(any(), any(), eq(LoadRecordsResponse.class), any())).thenReturn(response);

        assertThatThrownBy(() -> sankhyaCityService.load(null, "0"))
                .isInstanceOf(SankhyaApiException.class);
    }

    @Test
    void findCityClientByCode_shouldReturnCity_whenFound() {
        LoadRecordsResponse response = buildResponse("1");
        City city = mock(City.class);

        when(sankhyaGateway.post(any(), any(), eq(LoadRecordsResponse.class), any())).thenReturn(response);
        when(cityMapper.toCityList(any())).thenReturn(List.of(city));

        City result = sankhyaCityService.findCityClientByCode("1");

        assertThat(result).isEqualTo(city);
    }

    @Test
    void findCityClientByCode_shouldThrowResourceNotFoundException_whenNotFound() {
        LoadRecordsResponse response = buildResponse("1");

        when(sankhyaGateway.post(any(), any(), eq(LoadRecordsResponse.class), any())).thenReturn(response);
        when(cityMapper.toCityList(any())).thenReturn(List.of());

        assertThatThrownBy(() -> sankhyaCityService.findCityClientByCode("99"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("City not found in Sankhya");
    }
}
