package com.br.vidya.repository;

import com.br.vidya.model.City;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CityRepository extends CreateOrUpdateRepository<City, Long>, JpaSpecificationExecutor<City> {
}
