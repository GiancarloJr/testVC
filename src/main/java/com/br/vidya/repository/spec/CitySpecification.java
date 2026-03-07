package com.br.vidya.repository.spec;

import com.br.vidya.model.City;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class CitySpecification {

    private CitySpecification() {
    }

    public static Specification<City> withFilters(String name, String codCid) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%"));
            }

            if (codCid != null && !codCid.isBlank()) {
                try {
                    predicates.add(cb.equal(root.get("codCid"), Long.parseLong(codCid.trim())));
                } catch (NumberFormatException ignored) {
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
