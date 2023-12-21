package com.casamancaise.services;

import com.casamancaise.dao.InventaireRepository;
import com.casamancaise.dao.MouvementRepository;
import com.casamancaise.dao.VenteRepository;
import com.casamancaise.dto.DetailVenteDto;
import com.casamancaise.dto.VenteDto;
import com.casamancaise.entities.*;
import com.casamancaise.mapping.DetailVenteMapper;
import com.casamancaise.mapping.VenteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VenteServiceImpl implements VenteService {
    private static final Logger logger = LoggerFactory.getLogger(VenteServiceImpl.class);

    @Autowired
    private VenteRepository venteRepository;
    @Autowired
    private VenteMapper venteMapper;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private InventaireRepository inventaireRepository;
    @Autowired
    private DetailVenteMapper detailVenteMapper;
    @Override
    public VenteDto saveVente(VenteDto venteDto) {
        logger.info("Début de la méthode saveVente avec venteDto: {}", venteDto);
        LocalDate today = LocalDate.now();
// Vérifier la disponibilité du stock avant de continuer
        if (!isStockAvailableForVente(venteDto)) {
            logger.error("Vente non réalisée en raison d'un stock insuffisant.");
            throw new IllegalStateException("Stock insuffisant pour réaliser la vente.");
        }
        // Convertir DTO en entité
        Vente vente = venteMapper.toEntity(venteDto);
        vente.setReference(generateReference());
        vente.setDateVente(today);

        // Associer les détails de vente à l'entité Vente
        if (venteDto.getDetailVentes() != null && !venteDto.getDetailVentes().isEmpty()) {
            vente.getDetailVentes().clear(); // Nettoyer les anciens détails si nécessaire
            for (DetailVenteDto detailDto : venteDto.getDetailVentes()) {
                logger.debug("Traitement du DetailVenteDto : {}", detailDto);
                DetailVente detail = detailVenteMapper.toEntity(detailDto);
                detail.setVente(vente); // Associer chaque détail à la vente
                vente.getDetailVentes().add(detail);
            }
        }

        // Sauvegarder l'entité de vente avec tous ses détails
        vente = venteRepository.save(vente);

        // Mettre à jour l'inventaire et créer les mouvements pour chaque détail de vente
        if (venteDto.getDetailVentes() != null && !venteDto.getDetailVentes().isEmpty()) {
            for (DetailVente detail : vente.getDetailVentes()) {
                updateInventoryAndCreateMovement(detail, vente);
            }
        }

        logger.debug("Entité Vente après la sauvegarde avec ID : {}", vente.getId());
        if (vente.getId() == null) {
            logger.error("L'ID de Vente est null après la sauvegarde.");
            throw new IllegalStateException("L'ID de Vente ne peut pas être null après la sauvegarde.");
        }

        VenteDto savedVenteDto = venteMapper.toDto(vente);
        logger.info("Fin de la méthode saveVente avec savedVenteDto : {}", savedVenteDto);
        return savedVenteDto;
    }
    public boolean isStockAvailableForVente(VenteDto venteDto) {
        logger.info("Vérification de la disponibilité du stock pour la vente ID: {}", venteDto.getId());
        for (DetailVenteDto detailVenteDto : venteDto.getDetailVentes()) {
            Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                            detailVenteDto.getArticleId(), Math.toIntExact(venteDto.getEntrepotId()))
                    .orElse(null);

            if (inventaire == null || inventaire.getQuantiteConforme() < detailVenteDto.getQuantity()) {
                logger.warn("Stock insuffisant pour l'article ID: {} dans l'entrepôt ID: {}",
                        detailVenteDto.getArticleId(), venteDto.getEntrepotId());
                return false;
            }
        }
        logger.info("Stock suffisant pour réaliser la vente ID: {}", venteDto.getId());
        return true;
    }
    private void updateInventoryAndCreateMovement(DetailVente detail, Vente vente) {
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(), vente.getEntrepot().getIdEntre())
                .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: " + detail.getArticle().getIdArticle()));

        // Ajuster l'inventaire pour la quantité vendue
        int quantityChange = -detail.getQuantity(); // Quantité négative pour une vente
        inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + quantityChange);
        inventaireRepository.save(inventaire);
        // Créer un mouvement pour la vente
        Mouvement mouvement = new Mouvement();
        mouvement.setInventaire(inventaire);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(quantityChange); // Quantité négative pour une sortie
        mouvement.setCondition("CONFORME"); // Ou selon la condition de l'article vendu
        mouvement.setType(TypeMouvement.SORTIE); // Vente est une sortie de stock
        mouvement.setReference(vente.getReference());
        mouvementRepository.save(mouvement);
    }
    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        logger.info("Nombre de ventes pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = venteRepository.countVenteForToday() + 1;
        return "VD" + datePart + String.format("%04d", count);
    }
    @Override
    public VenteDto getVenteById(Long id) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée avec l'id: " + id));
        return venteMapper.toDto(vente);
    }
    @Override
    public void deleteVente(Long id) {
        venteRepository.deleteById(id);
    }
    @Override
    public List<VenteDto> getAllVentes() {
        return venteRepository.findAll().stream()
                .map(venteMapper::toDto)
                .toList();
    }
    @Override
    public Optional<VenteDto> findByReference(String reference) {
        return venteRepository.findByReference(reference)
                .map(venteMapper::toDto);
    }

}
