package com.casamancaise.services;

import com.casamancaise.dao.DotationRepository;
import com.casamancaise.dao.InventaireRepository;
import com.casamancaise.dao.MouvementRepository;
import com.casamancaise.dto.DetailsDotationDto;
import com.casamancaise.dto.DotationDto;
import com.casamancaise.entities.DetailsDotation;
import com.casamancaise.entities.Dotation;
import com.casamancaise.entities.Inventaire;
import com.casamancaise.entities.Mouvement;
import com.casamancaise.enums.Etat;
import com.casamancaise.enums.TypeMouvement;
import com.casamancaise.mapping.DetailsDotationMapper;
import com.casamancaise.mapping.DotationMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DotationServiceImpl implements DotationService {
    private static final Logger logger = LoggerFactory.getLogger(DotationServiceImpl.class);

    private final DotationRepository dotationRepository;

    private final DetailsDotationMapper detailsDotationMapper;
    private final DotationMapper dotationMapper;
    private final MouvementRepository mouvementRepository;
    private final InventaireRepository inventaireRepository;

    public DotationServiceImpl(DotationRepository dotationRepository, DetailsDotationMapper detailsDotationMapper, DotationMapper dotationMapper, MouvementRepository mouvementRepository, InventaireRepository inventaireRepository) {
        this.dotationRepository = dotationRepository;
        this.detailsDotationMapper = detailsDotationMapper;
        this.dotationMapper = dotationMapper;
        this.mouvementRepository = mouvementRepository;
        this.inventaireRepository = inventaireRepository;
    }

    @Override
    public DotationDto saveDotation(DotationDto dotationDto) {
        logger.info("Début de la méthode saveDotation avec dotationDto: {}", dotationDto);
        LocalDate today = LocalDate.now();
        // Convertir DTO en entité
        Dotation dotation = dotationMapper.toEntity(dotationDto);
        dotation.setReference(generateReference());
        dotation.setDateDotation(today);
        // Associer les détails de dotation à l'entité Dotation
        if (dotationDto.getDetailsDotation() != null && !dotationDto.getDetailsDotation().isEmpty()) {
            dotation.getDetailsDotation().clear(); // Nettoyer les anciens détails si nécessaire
            for (DetailsDotationDto detailDto : dotationDto.getDetailsDotation()) {
                logger.debug("Traitement du DetailsDotationDto : {}", detailDto);
                DetailsDotation detail = detailsDotationMapper.toEntity(detailDto);
                detail.setDotation(dotation); // Associer chaque détail à la dotation
                dotation.getDetailsDotation().add(detail);
            }
        }
        // Sauvegarder l'entité de dotation avec tous ses détails
        dotation = dotationRepository.save(dotation);
        // Mettre à jour l'inventaire et créer les mouvements pour chaque détail de dotation
        for (DetailsDotation detail : dotation.getDetailsDotation()) {
            updateInventoryAndCreateMovement(detail, dotation);
        }

        logger.debug("Entité Dotation après la sauvegarde avec ID : {}", dotation.getId());
        if (dotation.getId() == null) {
            logger.error("L'ID de Dotation est null après la sauvegarde.");
            throw new IllegalStateException("L'ID de Dotation ne peut pas être null après la sauvegarde.");
        }
        DotationDto savedDotationDto = dotationMapper.toDto(dotation);
        logger.info("Fin de la méthode saveDotation avec savedDotationDto : {}", savedDotationDto);
        return savedDotationDto;
    }

    private void updateInventoryAndCreateMovement(DetailsDotation detail, Dotation dotation) {
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(), dotation.getEntrepot().getIdEntre())
                .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: " + detail.getArticle().getIdArticle()));
        // Ajuster l'inventaire pour la quantité dotée
        int quantityChange = -detail.getQuantity(); // Quantité négative pour une dotation
        if (Etat.CONFORME.equals(detail.getEtat())) {
            inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + quantityChange);
        } else {
            inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() + quantityChange);
        }
        inventaireRepository.save(inventaire);
        // Créer un mouvement pour la dotation
        Mouvement mouvement = new Mouvement();
        mouvement.setInventaire(inventaire);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(quantityChange); // Quantité négative pour une sortie
        mouvement.setCondition(detail.getEtat().name()); // Utiliser l'état de la dotation
        mouvement.setType(TypeMouvement.SORTIE); // Dotation est une sortie de stock
        mouvement.setReference(dotation.getReference());
        mouvementRepository.save(mouvement);
    }

    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        logger.info("Nombre de dotations pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = dotationRepository.countDotationForToday() + 1;
        return "DT" + datePart + String.format("%04d", count);
    }

    @Override
    public DotationDto getDotationById(Long id) {
        Dotation dotation = dotationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dotation non trouvée avec l'id: " + id));
        return dotationMapper.toDto(dotation);
    }

    @Override
    public void deleteDotation(Long id) {
        dotationRepository.deleteById(id);
    }

    @Override
    public List<DotationDto> getAllDotations() {
        return dotationRepository.findAll().stream()
                .map(dotationMapper::toDto)
                .toList();
    }

    @Override
    public Optional<DotationDto> findByReference(String reference) {
        return dotationRepository.findByReference(reference)
                .map(dotationMapper::toDto);
    }
}
