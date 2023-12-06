package com.casamancaise.services;

import com.casamancaise.dao.DotationRepository;
import com.casamancaise.dto.DotationDto;
import com.casamancaise.mapping.DotationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DotationServiceImpl implements DotationService {

    private final DotationRepository dotationRepository;
    private final DotationMapper dotationMapper;

    @Autowired
    public DotationServiceImpl(DotationRepository dotationRepository, DotationMapper dotationMapper) {
        this.dotationRepository = dotationRepository;
        this.dotationMapper = dotationMapper;
    }


    @Override
    public DotationDto createDotation(DotationDto dotationDto) {
        return null;
    }

    @Override
    public DotationDto updateDotation(Long id, DotationDto dotationDto) {
        return null;
    }

    @Override
    public DotationDto getDotationById(Long id) {
        return null;
    }

    @Override
    public List<DotationDto> getAllDotations() {
        return null;
    }

    @Override
    public void deleteDotation(Long id) {

    }
}