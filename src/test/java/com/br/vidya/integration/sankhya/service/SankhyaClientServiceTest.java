package com.br.vidya.integration.sankhya.service;

import com.br.vidya.integration.sankhya.dto.LoadRecordsResponse;
import com.br.vidya.integration.sankhya.dto.SaveRecordRequest;
import com.br.vidya.integration.sankhya.dto.SaveRecordResponse;
import com.br.vidya.integration.sankhya.exceptions.SankhyaApiException;
import com.br.vidya.integration.sankhya.gateway.SankhyaGateway;
import com.br.vidya.mapper.ClientMapper;
import com.br.vidya.model.Client;
import com.br.vidya.repository.CityRepository;
import com.br.vidya.repository.ClientRepository;
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
class SankhyaClientServiceTest {

    @Mock
    private SankhyaGateway sankhyaGateway;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private SankhyaCityService sankhyaCityService;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SankhyaClientService sankhyaClientService;

    private LoadRecordsResponse buildResponse(String status) {
        var entities = new LoadRecordsResponse.Entities(null, null, null, null, null, null);
        var responseBody = new LoadRecordsResponse.ResponseBody(entities);
        return new LoadRecordsResponse(null, status, null, null, null, responseBody);
    }

    @Test
    void syncClients_shouldReturnClients() {
        LoadRecordsResponse response = buildResponse("1");
        when(sankhyaGateway.post(any(), any(), eq(LoadRecordsResponse.class), any())).thenReturn(response);
        List<Client> result = sankhyaClientService.syncClients(null, null, "0");

        assertThat(result).isEmpty();
        verify(clientRepository).saveOrUpdateAll(eq(List.of()), any());
        verify(entityManager).flush();
    }

    @Test
    void load_shouldReturnResponse_whenStatusIsOne() {
        LoadRecordsResponse response = buildResponse("1");

        when(sankhyaGateway.post(any(), any(), eq(LoadRecordsResponse.class), any())).thenReturn(response);

        LoadRecordsResponse result = sankhyaClientService.load(null, null, "0");

        assertThat(result).isEqualTo(response);
    }

    @Test
    void load_shouldThrowSankhyaApiException_whenStatusIsZero() {
        LoadRecordsResponse response = new LoadRecordsResponse(null, "0", null, null, "Error", null);

        when(sankhyaGateway.post(any(), any(), eq(LoadRecordsResponse.class), any())).thenReturn(response);

        assertThatThrownBy(() -> sankhyaClientService.load(null, null, "0"))
                .isInstanceOf(SankhyaApiException.class);
    }

    @Test
    void save_shouldReturnSaveRecordResponse() {
        SaveRecordRequest request = mock(SaveRecordRequest.class);
        SaveRecordResponse response = new SaveRecordResponse(null, "1", null, null, null, null);

        when(sankhyaGateway.post(eq(request), any(), eq(SaveRecordResponse.class), any())).thenReturn(response);

        SaveRecordResponse result = sankhyaClientService.save(request);

        assertThat(result).isEqualTo(response);
        verify(sankhyaGateway).post(eq(request), any(), eq(SaveRecordResponse.class), any());
    }

    @Test
    void save_shouldThrowSankhyaApiException_whenStatusIsZero() {
        SaveRecordRequest request = mock(SaveRecordRequest.class);
        SaveRecordResponse response = new SaveRecordResponse(null, "0", null, null, "Error", null);

        when(sankhyaGateway.post(eq(request), any(), eq(SaveRecordResponse.class), any())).thenReturn(response);

        assertThatThrownBy(() -> sankhyaClientService.save(request))
                .isInstanceOf(SankhyaApiException.class);
    }
}
