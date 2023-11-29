package com.casamancaise.services;

import com.casamancaise.dao.InventaireRepository;
import com.casamancaise.dto.InventaireDto;
import com.casamancaise.entities.Article;
import com.casamancaise.entities.Entrepot;
import com.casamancaise.entities.Inventaire;
import com.casamancaise.mapping.InventaireMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventaireServiceImpl implements InventaireService {

    private final InventaireRepository inventaireRepository;
    private final InventaireMapper inventaireMapper;

    @Autowired
    public InventaireServiceImpl(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper) {
        this.inventaireRepository = inventaireRepository;
        this.inventaireMapper = inventaireMapper;
    }

    @Override
    @Transactional
    public InventaireDto updateInventory(Long idArticle, int idEntre, int quantiteConforme, int quantiteNonConforme) {
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(idArticle,idEntre)
                .orElse(new Inventaire());
        inventaire.setArticle(new Article(idArticle));
        inventaire.setEntrepot(new Entrepot(idEntre));
        inventaire.setQuantiteConforme(quantiteConforme);
        inventaire.setQuantiteNonConforme(quantiteNonConforme);
        Inventaire savedInventaire = inventaireRepository.save(inventaire);
        return inventaireMapper.toDto(savedInventaire);
    }
    @Override
    public InventaireDto getInventoryByArticleAndEntrepot(Long idArticle, int idEntre) {
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(idArticle,idEntre)
                .orElseThrow(() -> new IllegalStateException("Inventaire non trouvé pour l'article id: " + idArticle+ " et entrepôt id: " + idEntre));
        return inventaireMapper.toDto(inventaire);
    }

    @Override
    public List<InventaireDto> getAllInventoryItems() {
        List<Inventaire> inventaireList = inventaireRepository.findAll();
        return inventaireList.stream()
                .map(inventaireMapper::toDto)
                .collect(Collectors.toList());
    }
}
