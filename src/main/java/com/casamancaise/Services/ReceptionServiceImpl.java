package com.casamancaise.services;

import com.casamancaise.dao.InventaireRepository;
import com.casamancaise.dao.MouvementRepository;
import com.casamancaise.dao.ReceptionDetailRepository;
import com.casamancaise.dao.ReceptionStockRepository;
import com.casamancaise.dto.ReceptionDetailDto;
import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.entities.*;
import com.casamancaise.mapping.ReceptionDetailMapper;
import com.casamancaise.mapping.ReceptionStockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReceptionServiceImpl implements ReceptionService {
    private static final Logger logger = LoggerFactory.getLogger(ReceptionServiceImpl.class);
    @Autowired
    private ReceptionStockRepository receptionStockRepository;
    @Autowired
    private ReceptionDetailRepository receptionDetailRepository;
    @Autowired
    private ReceptionStockMapper receptionStockMapper;
    @Autowired
    private ReceptionDetailMapper receptionDetailMapper;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private InventaireRepository inventaireRepository;

    public ReceptionStockDto saveReception(ReceptionStockDto receptionStockDto) {
        logger.info("Début de la méthode saveReception avec receptionStockDto: {}", receptionStockDto);

        // Convertir DTO en entité
        ReceptionStock receptionStock = receptionStockMapper.toEntity(receptionStockDto);

        // Associer les détails de réception à l'entité ReceptionStock
        if (receptionStockDto.getReceptionDetails() != null && !receptionStockDto.getReceptionDetails().isEmpty()) {
            receptionStock.getReceptionDetails().clear(); // Nettoyer les anciens détails si nécessaire
            for (ReceptionDetailDto detailDto : receptionStockDto.getReceptionDetails()) {
                logger.debug("Traitement du ReceptionDetailDto : {}", detailDto);
                ReceptionDetail detail = receptionDetailMapper.toEntity(detailDto);
                detail.setReceptionStock(receptionStock); // Associer chaque détail à la réception
                receptionStock.getReceptionDetails().add(detail);
            }
        }

        // Sauvegarder l'entité de réception avec tous ses détails
        receptionStock = receptionStockRepository.save(receptionStock);

        // Maintenant que ReceptionStock est sauvegardé, mettre à jour l'inventaire et créer les mouvements
        if (receptionStockDto.getReceptionDetails() != null && !receptionStockDto.getReceptionDetails().isEmpty()) {
            for (ReceptionDetail detail : receptionStock.getReceptionDetails()) {
                // Mettre à jour l'inventaire et créer un mouvement pour chaque détail
                updateInventoryAndCreateMovement(detail, receptionStock);
            }
        }

        logger.debug("Entité ReceptionStock après la sauvegarde avec ID : {}", receptionStock.getId());
        if (receptionStock.getId() == null) {
            logger.error("L'ID de ReceptionStock est null après la sauvegarde.");
            throw new IllegalStateException("L'ID de ReceptionStock ne peut pas être null après la sauvegarde.");
        }

        ReceptionStockDto savedReceptionStockDto = receptionStockMapper.toDto(receptionStock);
        logger.info("Fin de la méthode saveReception avec savedReceptionStockDto : {}", savedReceptionStockDto);
        return savedReceptionStockDto;
    }

    private void updateInventoryAndCreateMovement(ReceptionDetail detail, ReceptionStock receptionStock) {
        // Rechercher l'Inventaire existant ou en créer un nouveau si non trouvé
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(detail.getArticle().getIdArticle(), receptionStock.getEntrepot().getIdEntre())
                .orElseGet(() -> new Inventaire(null, receptionStock.getEntrepot(), detail.getArticle(), 0, 0));
        int quantityChange = detail.getQuantity();
        if (Etat.CONFORME.equals(detail.getEtat())) {
            inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + quantityChange);
        } else {
            inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() + quantityChange);
        }
        inventaireRepository.save(inventaire);
        Mouvement mouvement = new Mouvement();
        mouvement.setInventaire(inventaire);
        mouvement.setDateMouvement(LocalDate.now());
        mouvement.setQuantiteChange(quantityChange);
        mouvement.setCondition(detail.getEtat().name());
        mouvement.setType(TypeMouvement.ENTREE);
        mouvement.setReceptionStockMv(receptionStock);
        mouvementRepository.save(mouvement);
    }

    public ReceptionStockDto getReceptionById(Long id) {
        ReceptionStock receptionStock = receptionStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réception  non trouvée avec l'id: " + id));
        return receptionStockMapper.toDto(receptionStock);
    }

    public void deleteReception(Long id) {
        receptionStockRepository.deleteById(id);
    }

    public List<ReceptionStockDto> getAllReceptions() {
        return receptionStockRepository.findAll().stream()
                .map(receptionStockMapper::toDto)
                .toList();
    }

    @Transactional
    public ReceptionStockDto updateReception(Long id, ReceptionStockDto receptionStockDto) {
        logger.info("Début de la méthode updateReception avec id: {} et receptionStockDto: {}", id, receptionStockDto);

        // Trouver la réception existante ou jeter une exception si non trouvée
        ReceptionStock receptionStock = receptionStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réception de stock non trouvée avec l'id: " + id));

        // Mettre à jour l'entité ReceptionStock avec les nouvelles valeurs du DTO (sauf les détails)
        receptionStockMapper.updateFromDto(receptionStockDto, receptionStock);

        // Traitement des détails de la réception
        List<Long> detailIds = new ArrayList<>();
        if (receptionStockDto.getReceptionDetails() != null) {
            for (ReceptionDetailDto detailDto : receptionStockDto.getReceptionDetails()) {
                ReceptionDetail detail;
                if (detailDto.getId() != null) {
                    // Mettre à jour le détail existant
                    detail = receptionDetailRepository.findById(detailDto.getId())
                            .orElseThrow(() -> new RuntimeException("Détail de réception non trouvé avec l'id: " + detailDto.getId()));
                    receptionDetailMapper.updateFromDto(detailDto, detail);
                } else {
                    // Créer un nouveau détail
                    detail = receptionDetailMapper.toEntity(detailDto);
                    detail.setReceptionStock(receptionStock);
                }
                detailIds.add(detail.getId());

                // Mettre à jour l'inventaire et créer/mettre à jour le mouvement
                updateInventoryAndCreateMovement(detail, receptionStock);
            }
        }

        // Supprimer les détails qui ne sont plus présents
        receptionStock.getReceptionDetails().removeIf(detail -> !detailIds.contains(detail.getId()));

        // Sauvegarder les changements dans la réception stock
        receptionStock = receptionStockRepository.save(receptionStock);

        // Convertir en DTO pour le retour
        ReceptionStockDto savedReceptionStockDto = receptionStockMapper.toDto(receptionStock);
        logger.info("Fin de la méthode updateReception avec savedReceptionStockDto : {}", savedReceptionStockDto);
        return savedReceptionStockDto;
    }
}


