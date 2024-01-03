package com.casamancaise.services;

import com.casamancaise.dto.CanalDistribDto;

import java.util.List;

public interface CanalDistribService {
    CanalDistribDto createCanal(CanalDistribDto canalDistribDto);

    CanalDistribDto getCanalById(Integer id);

    List<CanalDistribDto> getAllCanals();

    CanalDistribDto updateCanal(Integer id, CanalDistribDto canalDistribDto);

    void deleteCanal(Integer id);
}
