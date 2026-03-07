package com.br.vidya.service;

import com.br.vidya.dto.request.ClientRequest;
import com.br.vidya.dto.response.ClientResponse;
import com.br.vidya.dto.response.PageResponse;
import com.br.vidya.integration.sankhya.dto.SaveRecordRequest;
import com.br.vidya.integration.sankhya.dto.SaveRecordResponse;
import com.br.vidya.integration.sankhya.service.SankhyaCityService;
import com.br.vidya.integration.sankhya.service.SankhyaClientService;
import com.br.vidya.mapper.ClientMapper;
import com.br.vidya.model.City;
import com.br.vidya.model.Client;
import com.br.vidya.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private SankhyaClientService sankhyaClientService;

    @Mock
    private SankhyaCityService sankhyaCityService;

    @InjectMocks
    private ClientService clientService;

    @Test
    void create_shouldReturnClientResponse() {
        ClientRequest request = mock(ClientRequest.class);
        City city = mock(City.class);
        Client client = mock(Client.class);
        ClientResponse expected = mock(ClientResponse.class);
        SaveRecordRequest saveRecordRequest = mock(SaveRecordRequest.class);
        SaveRecordResponse saveRecordResponse = mock(SaveRecordResponse.class);

        when(request.codCid()).thenReturn(1L);
        when(sankhyaCityService.findCityClientByCode("1")).thenReturn(city);
        when(clientMapper.toEntity(request)).thenReturn(client);
        when(clientMapper.toSaveRecordRequest(request)).thenReturn(saveRecordRequest);
        when(sankhyaClientService.save(saveRecordRequest)).thenReturn(saveRecordResponse);

        var responseBody = mock(SaveRecordResponse.ResponseBody.class);
        var entities = mock(SaveRecordResponse.Entities.class);
        var entity = mock(SaveRecordResponse.Entity.class);
        var codParc = mock(SaveRecordResponse.SmField.class);

        when(saveRecordResponse.responseBody()).thenReturn(responseBody);
        when(responseBody.entities()).thenReturn(entities);
        when(entities.entity()).thenReturn(entity);
        when(entity.codParc()).thenReturn(codParc);
        when(codParc.value()).thenReturn("123");

        when(clientMapper.toResponse(client)).thenReturn(expected);

        ClientResponse result = clientService.create(request);

        assertThat(result).isEqualTo(expected);
        verify(clientRepository).save(client);
        verify(client).setCity(city);
        verify(client).setCodParc(123L);
    }

    @Test
    void getClients_shouldReturnPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Client client = mock(Client.class);
        ClientResponse clientResponse = mock(ClientResponse.class);

        when(sankhyaClientService.syncClients("1", null, "0")).thenReturn(List.of(client));
        when(clientMapper.toResponse(client)).thenReturn(clientResponse);

        PageResponse<ClientResponse> result = clientService.getClients("1", null, pageable);

        assertThat(result).isNotNull();
        assertThat(result.data()).hasSize(1);
        verify(sankhyaClientService).syncClients("1", null, "0");
    }

    @Test
    void getClients_shouldReturnEmptyPage_whenNoResults() {
        Pageable pageable = PageRequest.of(0, 10);

        when(sankhyaClientService.syncClients(null, null, "0")).thenReturn(List.of());

        PageResponse<ClientResponse> result = clientService.getClients(null, null, pageable);

        assertThat(result).isNotNull();
        assertThat(result.data()).isEmpty();
    }
}
