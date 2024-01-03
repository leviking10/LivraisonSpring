package com.casamancaise.services;

import com.casamancaise.dao.AnnulationRepository;
import com.casamancaise.dao.InventaireRepository;
import com.casamancaise.dao.MouvementRepository;
import com.casamancaise.dao.ReceptionStockRepository;
import com.casamancaise.dto.ReceptionDetailDto;
import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.entities.*;
import com.casamancaise.enums.Etat;
import com.casamancaise.enums.TypeMouvement;
import com.casamancaise.mapping.ReceptionDetailMapper;
import com.casamancaise.mapping.ReceptionStockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReceptionServiceImpl implements ReceptionService {
    private static final Logger logger = LoggerFactory.getLogger(ReceptionServiceImpl.class);
    private final ReceptionStockRepository receptionStockRepository;
    private final ReceptionStockMapper receptionStockMapper;

    private final ReceptionDetailMapper receptionDetailMapper;

    private final AnnulationRepository annulationRepository;
    private final MouvementRepository mouvementRepository;
    private final InventaireRepository inventaireRepository;

    public ReceptionServiceImpl(ReceptionStockRepository receptionStockRepository, ReceptionStockMapper receptionStockMapper, ReceptionDetailMapper receptionDetailMapper, AnnulationRepository annulationRepository, MouvementRepository mouvementRepository, InventaireRepository inventaireRepository) {
        this.receptionStockRepository = receptionStockRepository;
        this.receptionStockMapper = receptionStockMapper;
        this.receptionDetailMapper = receptionDetailMapper;
        this.annulationRepository = annulationRepository;
        this.mouvementRepository = mouvementRepository;
        this.inventaireRepository = inventaireRepository;
    }

    @Override
    public ReceptionStockDto saveReception(ReceptionStockDto receptionStockDto) {
        logger.info("Début de la méthode saveReception avec receptionStockDto: {}", receptionStockDto);
        LocalDate today = LocalDate.now();
        // Convertir DTO en entité
        ReceptionStock receptionStock = receptionStockMapper.toEntity(receptionStockDto);
        receptionStock.setReference(generateReference());
        receptionStock.setDateReception(today);
        receptionStock.setEstAnnulee(false);
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

        // Mettre à jour l'inventaire et créer les mouvements
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

    @Override
    public void annulerReception(Long id, String raison) {
        ReceptionStock receptionStock = receptionStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réception non trouvée avec l'id: " + id));
        // Vérifier si la réception a déjà été annulée
        if (receptionStock.isEstAnnulee()) {
            throw new IllegalStateException("La réception avec l'id: " + id + " a déjà été annulée.");
        }
        // Mettre à jour l'état de la réception pour indiquer qu'elle a été annulée
        receptionStock.setEstAnnulee(true);
        receptionStockRepository.save(receptionStock);
        Annulation annulation = new Annulation();
        String refAnnulation = genererRefAnnulation(); // Générer une seule référence pour l'annulation
        annulation.setRef(refAnnulation);
        annulation.setRefOperation(receptionStock.getReference()); // Assurez-vous que cette référence est correctement définie
        annulation.setDateAnnulation(LocalDate.now());
        annulation.setRaison(raison);
        annulationRepository.save(annulation);
        // Inverser les effets de la réception sur l'inventaire
        for (ReceptionDetail detail : receptionStock.getReceptionDetails()) {
            inverserMouvementEtMettreAJourInventaire(detail);
        }
        logger.info("La réception a été annulée pour la raison: {}", raison);
    }

    private void inverserMouvementEtMettreAJourInventaire(ReceptionDetail detail) {
        // Trouver l'inventaire associé et ajuster les quantités
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(), detail.getReceptionStock().getEntrepot().getIdEntre())
                .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: "
                        + detail.getArticle().getIdArticle() + " et entrepôt: "
                        + detail.getReceptionStock().getEntrepot().getIdEntre()));
        // Ajuster les quantités conformes et non conformes
        int quantityChange = -detail.getQuantity();
        if (Etat.CONFORME.equals(detail.getEtat())) {
            inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + quantityChange);
        } else {
            inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() + quantityChange);
        }
        inventaireRepository.save(inventaire);
        // Créer un mouvement inverse pour annuler le mouvement précédent
        Mouvement mouvementInverse = new Mouvement();
        mouvementInverse.setInventaire(inventaire);
        mouvementInverse.setDateMouvement(LocalDateTime.now());
        mouvementInverse.setQuantiteChange(quantityChange);
        mouvementInverse.setCondition(detail.getEtat().name());
        mouvementInverse.setType(TypeMouvement.SORTIE);
        mouvementInverse.setReference(detail.getReceptionStock().getReference());
        mouvementRepository.save(mouvementInverse);
    }

    private String genererRefAnnulation() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = annulationRepository.countAnnulationForToday() + 1;
        return "AN" + datePart + String.format("%04d", count);
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
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(quantityChange);
        mouvement.setCondition(detail.getEtat().name());
        mouvement.setType(TypeMouvement.ENTREE);
        mouvement.setReference(receptionStock.getReference());
        mouvementRepository.save(mouvement);
    }

    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        logger.info("Nombre de reception pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = receptionStockRepository.countReceptionsForToday() + 1;
        return "RC" + datePart + String.format("%04d", count);
    }

    @Override
    public ReceptionStockDto getReceptionById(Long id) {
        ReceptionStock receptionStock = receptionStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réception  non trouvée avec l'id: " + id));
        return receptionStockMapper.toDto(receptionStock);
    }

    public void deleteReception(Long id) {
        receptionStockRepository.deleteById(id);
    }

    @Override
    public List<ReceptionStockDto> getAllReceptions() {
        return receptionStockRepository.findAll().stream()
                .map(receptionStockMapper::toDto)
                .toList();
    }

    @Override
    public Optional<ReceptionStockDto> findByReference(String reference) {
        return receptionStockRepository.findByReference(reference)
                .map(receptionStockMapper::toDto);
    }
}
