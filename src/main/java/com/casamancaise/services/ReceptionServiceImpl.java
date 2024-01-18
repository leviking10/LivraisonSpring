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
import com.casamancaise.myexeptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ReceptionServiceImpl implements ReceptionService {
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
        log.info("Début de la méthode saveReception avec receptionStockDto: {}", receptionStockDto);
        LocalDate today = LocalDate.now();
        // Validation de l'entrée (si nécessaire, ajoutez des validations supplémentaires)
        if (receptionStockDto == null || receptionStockDto.getReceptionDetails() == null) {
            throw new IllegalArgumentException("Les informations de réception sont manquantes ou incomplètes.");
        }
        // Convertir DTO en entité
        ReceptionStock receptionStock = receptionStockMapper.toEntity(receptionStockDto);
        String receptionReference = generateReference();
        receptionStock.setReference(receptionReference);
        receptionStock.setDateReception(today);
        receptionStock.setDeleted(false);
        // Associer les détails de réception et mettre à jour l'inventaire en une seule boucle
        receptionStock.getReceptionDetails().clear(); // Nettoyer les anciens détails si nécessaire
        for (ReceptionDetailDto detailDto : receptionStockDto.getReceptionDetails()) {
            log.debug("Traitement du ReceptionDetailDto : {}", detailDto);
            ReceptionDetail detail = receptionDetailMapper.toEntity(detailDto);
            detail.setReceptionStock(receptionStock); // Associer chaque détail à la réception
            receptionStock.getReceptionDetails().add(detail);
            // Mise à jour de l'inventaire et création de mouvements pour chaque détail
            updateInventory(detail, receptionStock, false);
            createMovement(detail, receptionStock, TypeMouvement.ENTREE, receptionReference);
        }
        // Sauvegarder l'entité de réception avec tous ses détails
        receptionStock = receptionStockRepository.save(receptionStock);
        log.debug("Entité ReceptionStock après la sauvegarde avec ID : {}", receptionStock.getId());
        ReceptionStockDto savedReceptionStockDto = receptionStockMapper.toDto(receptionStock);
        log.info("Fin de la méthode saveReception avec savedReceptionStockDto : {}", savedReceptionStockDto);
        return savedReceptionStockDto;
    }

    private void createMovement(ReceptionDetail detail, ReceptionStock receptionStock, TypeMouvement typeMouvement, String reference) {
        Mouvement mouvement = new Mouvement();
        mouvement.setInventaire(getOrCreateInventory(detail, receptionStock));
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(typeMouvement == TypeMouvement.SORTIE ? -detail.getQuantity() : detail.getQuantity());
        mouvement.setCondition(detail.getEtat().name());
        mouvement.setType(typeMouvement);
        mouvement.setReference(reference);
        mouvementRepository.save(mouvement);
    }

    private void updateInventory(ReceptionDetail detail, ReceptionStock receptionStock, boolean isCancellation) {
        Inventaire inventaire = getOrCreateInventory(detail, receptionStock);
        int quantityChange = isCancellation ? -detail.getQuantity() : detail.getQuantity();
        adjustInventoryQuantities(inventaire, quantityChange, detail.getEtat());
        inventaireRepository.save(inventaire);
    }

    private Inventaire getOrCreateInventory(ReceptionDetail detail, ReceptionStock receptionStock) {
        // Logique pour obtenir l'inventaire existant ou en créer un nouveau
        return inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(),
                        receptionStock.getEntrepot().getIdEntre())
                .orElseGet(() -> new Inventaire(null, receptionStock.getEntrepot(),
                        detail.getArticle(), 0, 0));
    }

    private void adjustInventoryQuantities(Inventaire inventaire, int quantityChange, Etat etat) {
        // Ajuster les quantités en fonction de l'état de l'article
        if (Etat.CONFORME.equals(etat)) {
            inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + quantityChange);
        } else {
            inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() + quantityChange);
        }
    }

    @Override
    public void annulerReception(Long id, String raison) {
        ReceptionStock receptionStock = receptionStockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Réception non trouvée avec l'id: " + id));
        verifyNotAlreadyCancelled(receptionStock, id);

        receptionStock.setDeleted(true);
        receptionStockRepository.save(receptionStock);
        String refAnnulation = genererRefAnnulation();
        Annulation annulation = createAnnulation(receptionStock, refAnnulation, raison);
        annulationRepository.save(annulation);

        receptionStock.getReceptionDetails().forEach(detail -> {
            updateInventory(detail, receptionStock, true);
            createMovement(detail, receptionStock, TypeMouvement.SORTIE, refAnnulation); // Utilisez refAnnulation ici
        });

        log.info("Réception ID {} a été annulée. Référence d'annulation générée: {}", id, refAnnulation);
    }

    private void verifyNotAlreadyCancelled(ReceptionStock receptionStock, Long id) {
        if (receptionStock.isDeleted()) {
            throw new IllegalStateException("La réception avec l'id: " + id + " a déjà été annulée.");
        }
    }

    private Annulation createAnnulation(ReceptionStock receptionStock, String refAnnulation, String raison) {
        Annulation annulation = new Annulation();
        annulation.setRef(refAnnulation);
        annulation.setRefOperation(receptionStock.getReference());
        annulation.setDateAnnulation(LocalDate.now());
        annulation.setRaison(raison);
        return annulation;
    }

    private String genererRefAnnulation() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = annulationRepository.countAnnulationForToday() + 1;
        return "AN" + datePart + String.format("%04d", count);
    }

    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("Nombre de reception pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = receptionStockRepository.countReceptionsForToday() + 1;
        return "RC" + datePart + String.format("%04d", count);
    }

    @Override
    public ReceptionStockDto getReceptionById(Long id) {
        ReceptionStock receptionStock = receptionStockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Réception non trouvée avec l'id: " + id));
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
