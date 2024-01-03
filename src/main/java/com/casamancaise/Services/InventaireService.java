package com.casamancaise.services;

import com.casamancaise.dto.InventaireDto;

import java.util.List;

public interface InventaireService {
    InventaireDto updateInventory(Long articleId, int entrepotId, int quantiteConforme, int quantiteNonConforme);

    InventaireDto getInventoryByArticleAndEntrepot(Long articleId, int entrepotId);

    List<InventaireDto> getAllInventoryItems();
}
