package com.br.vidya.integration.sankhya.support;

import com.br.vidya.integration.sankhya.dto.LoadRecordsRequest;

import java.util.*;

public final class SankhyaCriteriaBuilder {

    private final List<String> expressions = new ArrayList<>();
    private final List<LoadRecordsRequest.Parameter> parameters = new ArrayList<>();

    public SankhyaCriteriaBuilder addLike(String field, String value) {
        if (value != null && !value.isBlank()) {
            expressions.add(field + " LIKE ?");
            parameters.add(new LoadRecordsRequest.Parameter("S", "%" + value.trim() + "%"));
        }
        return this;
    }

    public SankhyaCriteriaBuilder addEquals(String field, String value, String type) {
        if (value != null && !value.isBlank()) {
            expressions.add(field + " = ?");
            parameters.add(new LoadRecordsRequest.Parameter(type, value.trim()));
        }
        return this;
    }

    public SankhyaCriteriaBuilder addIn(String field, String type, String... values) {
        if (values == null || values.length == 0) {
            return this;
        }

        List<String> validValues = Arrays.stream(values)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        if (validValues.isEmpty()) {
            return this;
        }

        String placeholders = String.join(", ", Collections.nCopies(validValues.size(), "?"));
        expressions.add(field + " IN (" + placeholders + ")");

        validValues.forEach(value ->
                parameters.add(new LoadRecordsRequest.Parameter(type, value))
        );

        return this;
    }

    public LoadRecordsRequest.Criteria build() {
        if (expressions.isEmpty()) {
            return null;
        }

        return new LoadRecordsRequest.Criteria(
                new LoadRecordsRequest.Expression(String.join(" AND ", expressions)),
                parameters
        );
    }
}
