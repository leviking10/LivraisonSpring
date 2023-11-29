package com.casamancaise.controller;
import com.casamancaise.dto.InventaireDto;
import com.casamancaise.services.InventaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventaires")
public class InventaireController {
    private final InventaireService inventaireService;
    @Autowired
    public InventaireController(InventaireService inventaireService) {
        this.inventaireService = inventaireService;
    }
    @GetMapping("/{articleId}/{entrepotId}")
    public ResponseEntity<InventaireDto> getInventoryByArticleAndEntrepot(
            @PathVariable Long articleId,
            @PathVariable int entrepotId) {
        InventaireDto inventaire = inventaireService.getInventoryByArticleAndEntrepot(articleId, entrepotId);
        return ResponseEntity.ok(inventaire);
    }
    @GetMapping
    public ResponseEntity<List<InventaireDto>> getAllInventoryItems() {
        List<InventaireDto> inventaireList = inventaireService.getAllInventoryItems();
        return ResponseEntity.ok(inventaireList);
    }
}