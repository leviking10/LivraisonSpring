package com.casamancaise.services;

import com.casamancaise.dto.ArticleDto;
import com.casamancaise.dto.EntrepotDto;
import com.casamancaise.dto.InventaireDto;
import com.casamancaise.entities.Article;
import com.casamancaise.entities.Entrepot;
import com.casamancaise.entities.Inventaire;

import java.util.List;

public interface InventaireService {
    InventaireDto updateInventory(Long articleId, int entrepotId, int quantiteConforme, int quantiteNonConforme);
    InventaireDto getInventoryByArticleAndEntrepot(Long articleId, int entrepotId);
    List<InventaireDto> getAllInventoryItems();
}
