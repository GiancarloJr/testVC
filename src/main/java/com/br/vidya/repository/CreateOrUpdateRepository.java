package com.br.vidya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.function.Function;

@NoRepositoryBean
public interface CreateOrUpdateRepository<T, ID> extends JpaRepository<T, ID> {

    default T saveOrUpdate(ID id, T entity) {
        if (id != null && existsById(id)) {
            return save(entity);
        }
        return save(entity);
    }

    default List<T> saveOrUpdateAll(List<T> entities, Function<T, ID> idExtractor) {
        return entities.stream()
                .map(entity -> saveOrUpdate(idExtractor.apply(entity), entity))
                .toList();
    }
}
