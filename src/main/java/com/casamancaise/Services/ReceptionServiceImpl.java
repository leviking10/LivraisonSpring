package com.casamancaise.services;

import com.casamancaise.dao.*;
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
import java.util.List;
@Service
@Transactional
public class ReceptionServiceImpl implements ReceptionService{
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
    @Override
    public ReceptionStockDto saveReception(ReceptionStockDto receptionStockDto) {
        logger.info("Début de la méthode saveReception avec receptionStockDto: {}", receptionStockDto);

        // Convertir DTO en entité et sauvegarder
        ReceptionStock receptionStock = receptionStockMapper.toEntity(receptionStockDto);
        logger.debug("Entité ReceptionStock avant la sauvegarde : {}", receptionStock);

        receptionStock = receptionStockRepository.save(receptionStock);
        logger.debug("Entité ReceptionStock après la sauvegarde avec ID : {}", receptionStock.getId());

        if (receptionStock.getId() == null) {
            logger.error("L'ID de ReceptionStock est null après la sauvegarde.");
            throw new IllegalStateException("L'ID de ReceptionStock ne peut pas être null après la sauvegarde.");
        }

        for (ReceptionDetailDto detailDto : receptionStockDto.getReceptionDetails()) {
            logger.debug("Traitement du ReceptionDetailDto : {}", detailDto);

            if (detailDto.getArticleId() == null) {
                logger.error("L'ID de l'article est null pour le détail de réception : {}", detailDto);
                throw new IllegalStateException("L'ID de l'article ne peut pas être null pour le détail de réception.");
            }

            ReceptionDetail detail = receptionDetailMapper.toEntity(detailDto);
            detail.setReceptionStock(receptionStock); // Associer avec le stock de réception sauvegardé

            logger.debug("Entité ReceptionDetail après conversion du DTO : {}", detail);

            // Vérification supplémentaire pour s'assurer que l'article est bien défini
            if (detail.getArticle() == null || detail.getArticle().getIdArticle() == null) {
                logger.error("Article ou ID d'article manquant pour le détail de réception : {}", detail);
                throw new IllegalStateException("Article ou ID d'article ne peut pas être null pour le détail de réception.");
            }

            detail = receptionDetailRepository.save(detail);
            logger.debug("Entité ReceptionDetail après la sauvegarde : {}", detail);

            updateInventoryAndCreateMovement(detail, receptionStock);
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
        mouvement.setReceptionStock(receptionStock);
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
        // Trouver la réception existante ou jeter une exception si non trouvée
        ReceptionStock receptionStock = receptionStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réception de stock non trouvée avec l'id: " + id));
        // La variable finalReceptionStock est utilisée pour conserver la référence à l'entité
        ReceptionStock finalReceptionStock = receptionStock;
        // Gérer les détails de la réception
        List<ReceptionDetail> updatedDetails = receptionStockDto.getReceptionDetails().stream()
                .map(detailDto -> {
                    ReceptionDetail detail = receptionDetailRepository.findById(detailDto.getId())
                            .map(existingDetail -> {
                                // Mettre à jour les détails existants avec les nouvelles valeurs du DTO
                                receptionDetailMapper.updateFromDto(detailDto, existingDetail);
                                return existingDetail;
                            })
                            .orElseGet(() -> {
                                // Créer de nouveaux détails si l'ID n'existe pas
                                ReceptionDetail newDetail = receptionDetailMapper.toEntity(detailDto);
                                newDetail.setReceptionStock(finalReceptionStock);
                                return newDetail;
                            });
                    // Mettre à jour l'inventaire et créer/mettre à jour le mouvement
                    updateInventoryAndCreateMovement(detail, finalReceptionStock);
                    return detail;
                })
                .toList();
        // Supprimer les détails qui ne sont plus présents
        List<Long> updatedDetailIds = updatedDetails.stream()
                .map(ReceptionDetail::getId)
                .toList();
        receptionStock.getReceptionDetails().removeIf(detail -> !updatedDetailIds.contains(detail.getId()));
        // Ajouter ou mettre à jour les détails
        receptionStock.setReceptionDetails(updatedDetails);
        // Sauvegarder les changements dans la réception stock
        receptionStock = receptionStockRepository.save(receptionStock);
        // Convertir en DTO pour le retour
        return receptionStockMapper.toDto(receptionStock);
    }
}


