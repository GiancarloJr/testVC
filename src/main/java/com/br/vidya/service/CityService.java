package com.br.vidya.service;

import com.br.vidya.dto.response.CityResponse;
import com.br.vidya.dto.response.PageResponse;
import com.br.vidya.integration.sankhya.service.SankhyaCityService;
import com.br.vidya.mapper.CityMapper;
import com.br.vidya.model.City;
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
public class CityService extends AbstractService<City, CityResponse> {

    private final CityMapper cityMapper;
    private final SankhyaCityService sankhyaCityService;

    @Transactional
    public PageResponse<CityResponse> getCities(String name, Pageable pageable, String... codCid) {
        List<City> cities = sankhyaCityService.sync(
                name,
                String.valueOf(pageable.getPageNumber()),
                codCid
        );

        Page<City> page = buildPage(cities, pageable);
        List<CityResponse> data = mapToResponseList(page, cityMapper::toResponse);

        return buildPageResponse(data, page);
    }

}
