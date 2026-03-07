package com.br.vidya.repository.spec;

import com.br.vidya.model.Client;
import com.br.vidya.model.enums.PersonType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ClientSpecification {

    private ClientSpecification() {
    }

    public static Specification<Client> withFilters(String nomeParc, String cgcCpf) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nomeParc != null && !nomeParc.isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("nomeParc")), "%" + nomeParc.toUpperCase() + "%"));
            }

            if (cgcCpf != null && !cgcCpf.isBlank()) {
                predicates.add(cb.equal(root.get("cgcCpf"), cgcCpf));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
