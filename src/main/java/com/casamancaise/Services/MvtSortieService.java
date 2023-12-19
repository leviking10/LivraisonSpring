package com.casamancaise.services;

import com.casamancaise.dto.MouvementSortieDto;

import java.util.List;
import java.util.Optional;

public interface MvtSortieService {
    MouvementSortieDto  SaveMvtSortie(MouvementSortieDto mvtSortieDto);
    MouvementSortieDto getMvtSortieById(Long id);
    List<MouvementSortieDto> getAllMvtSorties();
    Optional<MouvementSortieDto> findByReference(String reference);
}
