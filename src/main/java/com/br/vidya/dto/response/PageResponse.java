package com.br.vidya.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record PageResponse<T>(
        List<T> data,
        PageMeta meta
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PageMeta(
            Integer page,
            Integer size,
            Long totalElements,
            Integer totalPages
    ) {
        public PageMeta(int size, long totalElements) {
            this(null, size, totalElements, null);
        }
    }
}

