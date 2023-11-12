package com.casamancaise.Services;

import com.casamancaise.DTO.ArticleDto;
import com.casamancaise.DTO.FonctionDto;

import java.util.List;

public interface FonctionService {
    FonctionDto createFonction( FonctionDto fonctionDto);
    FonctionDto getFonctionById(Integer  id);
    List< FonctionDto> getAllFonctions();
    FonctionDto updateFonction(Integer id,  FonctionDto fonctionDto);
    void deleteFonction(Integer id);

}
