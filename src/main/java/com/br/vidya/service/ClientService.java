package com.br.vidya.service;

import com.br.vidya.dto.request.ClientRequest;
import com.br.vidya.integration.sankhya.dto.SaveRecordRequest;
import com.br.vidya.dto.response.ClientResponse;
import com.br.vidya.dto.response.PageResponse;
import com.br.vidya.integration.sankhya.dto.SaveRecordResponse;
import com.br.vidya.integration.sankhya.service.SankhyaCityService;
import com.br.vidya.integration.sankhya.service.SankhyaClientService;
import com.br.vidya.mapper.ClientMapper;
import com.br.vidya.model.City;
import com.br.vidya.model.Client;
import com.br.vidya.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService extends AbstractService<Client, ClientResponse> {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final SankhyaClientService sankhyaClientService;
    private final SankhyaCityService sankhyaCityService;

    @Transactional
    public ClientResponse create(ClientRequest request) {
        log.info("Creating client in local database");
        City city = sankhyaCityService.findCityClientByCode(String.valueOf(request.codCid()));

        Client client = clientMapper.toEntity(request);
        client.setCity(city);

        createClientSankhya(request, client);

        clientRepository.save(client);
        log.info("Save client in local database");

        return clientMapper.toResponse(client);
    }

    @Transactional
    public PageResponse<ClientResponse> getClients(String codParc, String cgcCpf, Pageable pageable) {
        log.info("Getting clients from Sankhya");
        List<Client> clients = sankhyaClientService.syncClients(
                codParc, cgcCpf,
                String.valueOf(pageable.getPageNumber())
        );

        Page<Client> page = buildPage(clients, pageable);
        List<ClientResponse> data = mapToResponseList(page, clientMapper::toResponse);

        return buildPageResponse(data, page);
    }

    private void createClientSankhya(ClientRequest request, Client client) {

        SaveRecordRequest saveRecordRequest = clientMapper.toSaveRecordRequest(request);
        SaveRecordResponse saveRecordResponse = sankhyaClientService.save(saveRecordRequest);
        String codParc = saveRecordResponse.responseBody().entities().entity().codParc().value();
        log.info("Save client in Sankhya CodParc: {}", codParc);

        client.setCodParc(Long.valueOf(codParc));
    }
}
