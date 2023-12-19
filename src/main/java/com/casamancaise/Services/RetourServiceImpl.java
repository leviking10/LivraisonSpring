package com.casamancaise.services;

import com.casamancaise.dao.RetourRepository;
import com.casamancaise.dto.RetourDto;
import com.casamancaise.entities.Retour;
import com.casamancaise.mapping.RetourMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RetourServiceImpl implements RetourService {
    @Autowired
    private RetourRepository retourRepository;

    @Autowired
    private RetourMapper retourMapper;

    @Override
    public RetourDto saveRetour(RetourDto retourDto) {
        Retour retour = retourMapper.toEntity(retourDto);
        retour = retourRepository.save(retour);
        return retourMapper.toDto(retour);
    }

    @Override
    public RetourDto getRetourById(Long id) {
        Retour retour = retourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retour not found"));
        return retourMapper.toDto(retour);
    }

    @Override
    public void deleteRetour(Long id) {
        retourRepository.deleteById(id);
    }

    @Override
    public List<RetourDto> getAllRetours() {
        return retourRepository.findAll().stream()
                .map(retourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RetourDto updateRetour(Long id, RetourDto retourDto) {
        Retour existingRetour = retourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retour not found"));
        retourMapper.updateFromDto(retourDto, existingRetour);
        existingRetour = retourRepository.save(existingRetour);
        return retourMapper.toDto(existingRetour);
    }
}
