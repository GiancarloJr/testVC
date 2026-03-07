package com.br.vidya.service;

import com.br.vidya.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractService<E, R> {

    protected Page<E> buildPage(List<E> items, Pageable pageable) {
        return new PageImpl<>(
                items,
                pageable,
                items.size()
        );
    }

    protected List<R> mapToResponseList(Page<E> page, Function<E, R> mapper) {
        return page.getContent().stream()
                .map(mapper)
                .toList();
    }

    protected PageResponse<R> buildPageResponse(List<R> data, Page<E> page) {
        return new PageResponse<>(
                data,
                new PageResponse.PageMeta(
                        page.getSize(),
                        page.getTotalElements()
                )
        );
    }
}
