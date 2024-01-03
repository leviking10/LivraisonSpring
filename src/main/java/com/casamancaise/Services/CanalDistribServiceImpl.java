package com.casamancaise.services;

import com.casamancaise.dao.CanalDistribRepository;
import com.casamancaise.dto.CanalDistribDto;
import com.casamancaise.entities.CanalDistrib;
import com.casamancaise.mapping.CanalDistribMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CanalDistribServiceImpl implements CanalDistribService {
    private final CanalDistribRepository canalDistribRepository;
    private final CanalDistribMapper canalDistribMapper;

    @Autowired
    public CanalDistribServiceImpl(CanalDistribRepository canalDistribRepository,
                                   CanalDistribMapper canalDistribMapper) {
        this.canalDistribRepository = canalDistribRepository;
        this.canalDistribMapper = canalDistribMapper;
    }

    @Override
    public CanalDistribDto createCanal(CanalDistribDto canalDistribDto) {
        CanalDistrib canalDistrib = canalDistribMapper.toEntity(canalDistribDto);
        canalDistrib = canalDistribRepository.save(canalDistrib);
        return canalDistribMapper.toDto(canalDistrib);
    }

    @Override
    public CanalDistribDto getCanalById(Integer id) {
        CanalDistrib canalDistrib = canalDistribRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CanalDistrib not found with id: " + id));
        return canalDistribMapper.toDto(canalDistrib);
    }

    @Override
    public List<CanalDistribDto> getAllCanals() {
        List<CanalDistrib> canals = canalDistribRepository.findAll();
        return canals.stream()
                .map(canalDistribMapper::toDto)
                .toList();
    }

    @Override
    public CanalDistribDto updateCanal(Integer id, CanalDistribDto canalDistribDto) {
        CanalDistrib existingCanal = canalDistribRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CanalDistrib not found with id: " + id));
        canalDistribMapper.updateCanalFromDto(canalDistribDto, existingCanal);
        existingCanal = canalDistribRepository.save(existingCanal);
        return canalDistribMapper.toDto(existingCanal);
    }

    @Override
    public void deleteCanal(Integer id) {
        canalDistribRepository.deleteById(id);
    }
}