package com.casamancaise.services;

import com.casamancaise.dao.MouvementRepository;
import com.casamancaise.dto.MouvementDto;
import com.casamancaise.entities.Mouvement;
import com.casamancaise.mapping.MouvementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MouvementServiceImpl implements MouvementService {

    private final MouvementRepository mouvementRepository;
    private final MouvementMapper mouvementMapper;

    @Autowired
    public MouvementServiceImpl(MouvementRepository mouvementRepository, MouvementMapper mouvementMapper) {
        this.mouvementRepository = mouvementRepository;
        this.mouvementMapper = mouvementMapper;
    }

    @Override
    @Transactional
    public MouvementDto recordMovement(MouvementDto mouvementDto) {
        Mouvement mouvement = mouvementMapper.toEntity(mouvementDto);
        mouvement = mouvementRepository.save(mouvement);
        return mouvementMapper.toDto(mouvement);
    }

    @Override
    public List<MouvementDto> getMovementsByArticle(Long articleId) {
        List<Mouvement> mouvements = mouvementRepository.findByArticleId(articleId);
        return mouvements.stream()
                .map(mouvementMapper::toDto)
                .toList();
    }

    @Override
    public List<MouvementDto> getMovementsByEntrepot(Integer entrepotId) {
        List<Mouvement> mouvements = mouvementRepository.findByEntrepotId(entrepotId);
        return mouvements.stream().map(mouvementMapper::toDto).toList();
    }

    @Override
    public List<MouvementDto> getMovementsByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Mouvement> mouvements = mouvementRepository.findByDateMouvementBetween(startDate, endDate);
        return mouvements.stream()
                .map(mouvementMapper::toDto)
                .toList();
    }
}
