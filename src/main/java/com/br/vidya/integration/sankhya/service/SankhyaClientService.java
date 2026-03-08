package com.br.vidya.integration.sankhya.service;

import com.br.vidya.integration.sankhya.dto.SaveRecordRequest;
import com.br.vidya.integration.sankhya.dto.SaveRecordResponse;
import com.br.vidya.integration.sankhya.gateway.SankhyaGateway;
import com.br.vidya.integration.sankhya.support.SankhyaCriteriaBuilder;
import com.br.vidya.mapper.ClientMapper;
import com.br.vidya.model.Client;
import com.br.vidya.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.br.vidya.integration.sankhya.dto.LoadRecordsRequest;
import com.br.vidya.integration.sankhya.dto.LoadRecordsResponse;
import com.br.vidya.model.City;
import com.br.vidya.repository.CityRepository;
import jakarta.persistence.EntityManager;

import java.util.*;

import static com.br.vidya.integration.sankhya.enums.EntityName.PARTNER;
import static com.br.vidya.integration.sankhya.enums.ServiceName.LOAD_RECORDS;
import static com.br.vidya.integration.sankhya.enums.ServiceName.SAVE_RECORD;

@Slf4j
@Service
@AllArgsConstructor
public class SankhyaClientService extends SankhyaAbstractService{

    private final SankhyaGateway sankhyaGateway;
    private final ClientRepository clientRepository;
    private final CityRepository cityRepository;
    private final ClientMapper clientMapper;
    private final SankhyaCityService sankhyaCityService;
    private final EntityManager entityManager;

    public List<Client> syncClients(String codParc, String cgcCpf, String offSetPage) {
        log.info("Syncing clients from Sankhya");
        LoadRecordsResponse response = load(codParc, cgcCpf, offSetPage);

        syncCities(response.responseBody().entities());
        List<Client> clients = mapClientsWithCities(response.responseBody().entities());

        clientRepository.saveOrUpdateAll(clients, Client::getCodParc);
        entityManager.flush();
        return clients;
    }

    public LoadRecordsResponse load(String codParc, String cgcCpf, String offSetPage) {
        LoadRecordsRequest.Criteria criteria = buildClientCriteria(codParc, cgcCpf);

        LoadRecordsRequest request = buildLoadRecordsRequest(
                PARTNER.getEntityName(),
                offSetPage,
                "CODPARC, NOMEPARC, CGC_CPF, RAZAOSOCIAL, TIPPESSOA, CLASSIFICMS, CODCID",
                criteria
        );

        LoadRecordsResponse response = sankhyaGateway.post(
                request,
                LOAD_RECORDS.getServiceName(),
                LoadRecordsResponse.class,
                "Error loading clients from Sankhya ERP"
        );

        validateCodeResponse(this.getClass().getSimpleName(),response.status(), response.statusMessage());

        return response;
    }

    public SaveRecordResponse save(SaveRecordRequest request) {
        SaveRecordResponse response = sankhyaGateway.post(
                request,
                SAVE_RECORD.getServiceName(),
                SaveRecordResponse.class,
                "Error saving client to Sankhya ERP"
        );

        validateCodeResponse(this.getClass().getSimpleName(),response.status(), response.statusMessage());

        return response;
    }

    private void syncCities(LoadRecordsResponse.Entities entities) {
        if (entities == null || entities.entity() == null) return;

        List<String> codCids = entities.entity().stream()
                .map(LoadRecordsResponse.Entity::f6)
                .filter(Objects::nonNull)
                .map(LoadRecordsResponse.SmField::value)
                .filter(Objects::nonNull)
                .filter(v -> !v.isBlank())
                .distinct()
                .toList();

        sankhyaCityService.sync(null, null, codCids.toArray(String[]::new));
        entityManager.flush();
    }

    private List<Client> mapClientsWithCities(LoadRecordsResponse.Entities entities) {
        if (entities == null || entities.entity() == null) return Collections.emptyList();
        return entities.entity().stream()
                .map(entity -> {
                    City city = null;

                    if (entity.f6() != null && entity.f6().value() != null) {
                        Long codCid = Long.parseLong(entity.f6().value());
                        city = cityRepository.findById(codCid).orElse(null);
                    }

                    return clientMapper.toClient(entity, city);
                })
                .toList();
    }

    private LoadRecordsRequest.Criteria buildClientCriteria(String codParc, String cgcCpf) {
        return new SankhyaCriteriaBuilder()
                .addIn("this.CODPARC", "I",codParc)
                .addEquals("this.CGC_CPF", cgcCpf, "S")
                .build();
    }
}
