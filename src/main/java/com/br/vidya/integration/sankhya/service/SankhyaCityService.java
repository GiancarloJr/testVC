package com.br.vidya.integration.sankhya.service;

import com.br.vidya.exception.ResourceNotFoundException;
import com.br.vidya.integration.sankhya.gateway.SankhyaGateway;
import com.br.vidya.integration.sankhya.support.SankhyaCriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.br.vidya.integration.sankhya.dto.LoadRecordsRequest;
import com.br.vidya.integration.sankhya.dto.LoadRecordsResponse;
import com.br.vidya.mapper.CityMapper;
import com.br.vidya.model.City;
import com.br.vidya.repository.CityRepository;
import jakarta.persistence.EntityManager;

import java.util.*;

import static com.br.vidya.integration.sankhya.enums.EntityName.CITY;
import static com.br.vidya.integration.sankhya.enums.ServiceName.LOAD_RECORDS;

@Slf4j
@Service
@AllArgsConstructor
public class SankhyaCityService extends SankhyaAbstractService{

    private final SankhyaGateway sankhyaGateway;
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final EntityManager entityManager;

    public List<City> sync(String name, String offSetPage, String... codCid) {
        LoadRecordsResponse response = load(name, offSetPage, codCid);

        List<City> cities = cityMapper.toCityList(response.responseBody().entities().entity());

        cityRepository.saveOrUpdateAll(cities, City::getCodCid);
        entityManager.flush();

        log.info("Successfully synced {} cities from Sankhya.", cities.size());
        return cities;
    }

    public LoadRecordsResponse load(String name, String offSetPage, String... codCid) {
        LoadRecordsRequest.Criteria criteria = buildCityCriteria(name, codCid);

        LoadRecordsRequest request = buildLoadRecordsRequest(
                CITY.getEntityName(),
                offSetPage,
                "CODCID,NOMECID,UF",
                criteria
        );

        LoadRecordsResponse response = sankhyaGateway.post(
                request,
                LOAD_RECORDS.getServiceName(),
                LoadRecordsResponse.class,
                "Error loading cities from Sankhya ERP"
        );

        validateCodeResponse(this.getClass().getSimpleName(),response.status(), response.statusMessage());

        return response;
    }

    public LoadRecordsRequest.Criteria buildCityCriteria(String name, String... codCid) {
        return new SankhyaCriteriaBuilder()
                .addIn("this.CODCID", "I", codCid)
                .addLike("this.NOMECID", name)
                .build();
    }

    public City findCityClientByCode(String codCid){
        return sync(null, null, String.valueOf(codCid))
                .stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("City not found in Sankhya, CodCid: " + codCid));
    }
}