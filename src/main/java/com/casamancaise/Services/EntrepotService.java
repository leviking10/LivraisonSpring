package com.casamancaise.services;

import com.casamancaise.dto.EntrepotDto;

import java.util.List;


public interface EntrepotService {
    EntrepotDto createEntrepot(EntrepotDto entrepotDto);

    EntrepotDto getEntrepotById(Integer id);

    List<EntrepotDto> getAllEntrepots();

    EntrepotDto updateEntrepot(Integer id, EntrepotDto entrepotDto);

    void deleteEntrepot(Integer id);
}
