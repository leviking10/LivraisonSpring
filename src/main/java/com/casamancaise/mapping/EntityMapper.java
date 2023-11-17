package com.casamancaise.mapping;

import java.util.List;

public interface EntityMapper <D, E> {
    E toEntity(D dto);
    D toDto(E entity);
    List<E> toEntity(List<D> todtoList);
    List<D> toDto(List<E> entityList);
}
