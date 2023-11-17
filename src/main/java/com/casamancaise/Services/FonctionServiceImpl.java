package com.casamancaise.services;
import com.casamancaise.dao.FonctionRepository;
import com.casamancaise.dto.FonctionDto;
import com.casamancaise.entities.Fonction;
import com.casamancaise.mapping.FonctionMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class FonctionServiceImpl implements FonctionService {
    private final FonctionRepository fonctionRepository;
    private final FonctionMapper fonctionMapper;
    @Autowired
    public FonctionServiceImpl(FonctionRepository fonctionRepository, FonctionMapper fonctionMapper) {
        this.fonctionRepository = fonctionRepository;
        this.fonctionMapper = fonctionMapper;
    }
    @Override
    @Transactional
    public FonctionDto createFonction(FonctionDto fonctionDto) {
        Fonction fonction = fonctionMapper.fonctionDTOToFonction(fonctionDto);
        fonction = fonctionRepository.save(fonction);
        return fonctionMapper.fonctionToFonctionDTO(fonction);
    }
    @Override
    @Transactional(readOnly = true)
    public FonctionDto getFonctionById(Integer  id) {
        Fonction fonction = fonctionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fonction not found with id: " + id));
        return fonctionMapper.fonctionToFonctionDTO(fonction);
    }
    @Override
    @Transactional
    public void deleteFonction(Integer id) {
        if (!fonctionRepository.existsById(id)) {
            throw new EntityNotFoundException("fonction not found with id: " + id);
        }
        fonctionRepository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public List<FonctionDto> getAllFonctions() {
        List<Fonction> fonctions= fonctionRepository.findAll();
        return fonctions.stream()
                .map(fonctionMapper::fonctionToFonctionDTO)
                .toList();
    }
    @Override
    @Transactional
    public FonctionDto updateFonction(Integer id, FonctionDto fonctionDto) {
        Fonction existingFonction = fonctionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fonction not found with id: " + id));
        // Utilisez le mapper pour mettre à jour les champs de l'article existant.
        fonctionMapper.updateFonctionFromDto(fonctionDto, existingFonction);
        // Sauvegardez les modifications
        Fonction updatedFonction = fonctionRepository.save(existingFonction);
        // Renvoyez le DTO mis à jour
        return fonctionMapper.fonctionToFonctionDTO(updatedFonction);
    }
}
