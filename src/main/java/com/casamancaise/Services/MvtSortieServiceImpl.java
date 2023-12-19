package com.casamancaise.services;

import com.casamancaise.dao.InventaireRepository;
import com.casamancaise.dao.MouvementRepository;
import com.casamancaise.dao.MouvementSortieRepository;
import com.casamancaise.dto.MouvementSortieDetailDto;
import com.casamancaise.dto.MouvementSortieDto;
import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.entities.*;
import com.casamancaise.mapping.MouvementSortieDetailMapper;
import com.casamancaise.mapping.MouvementSortieMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class MvtSortieServiceImpl implements MvtSortieService {
    private static final Logger logger = LoggerFactory.getLogger(MvtSortieServiceImpl.class);
    @Autowired
    private  MouvementSortieRepository mouvementSortieRepository;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private MouvementSortieDetailMapper mouvementSortieDetailMapper;
    @Autowired
    private InventaireRepository inventaireRepository;
    @Autowired
    private  MouvementSortieMapper mouvementSortieMapper;
    @Override
    public MouvementSortieDto SaveMvtSortie(MouvementSortieDto mvtSortieDto) {
        logger.info("Début de la méthode SaveMvtSortie avec mvtSortieDto: {}", mvtSortieDto);

        // Définir la date du jour
        LocalDate today = LocalDate.now();

        // Convertir DTO en entité
        MouvementSortie mvtSortie = mouvementSortieMapper.toEntity(mvtSortieDto);
        mvtSortie.setReference(generateReference());
        mvtSortie.setDateSortie(today);

        // Associer les détails de sortie à l'entité MouvementSortie

        if (mvtSortieDto.getDetailsSortie() != null && !mvtSortieDto.getDetailsSortie().isEmpty()) {
            mvtSortie.getDetailsSortie().clear(); // Nettoyer les anciens détails si nécessaire
            for (MouvementSortieDetailDto detailDto : mvtSortieDto.getDetailsSortie()) {
                logger.debug("Traitement du MouvementSortieDetailDto : {}", detailDto);
                MouvementSortieDetail detail = mouvementSortieDetailMapper.toEntity(detailDto);
                detail.setMouvementSortie(mvtSortie);
                mvtSortie.getDetailsSortie().add(detail);
            }
        }
        // Sauvegarder l'entité MouvementSortie avec tous ses détails
        mvtSortie = mouvementSortieRepository.save(mvtSortie);

        // Traiter la logique d'inventaire et de création de mouvements pour chaque détail
        if (mvtSortieDto.getDetailsSortie() != null && !mvtSortieDto.getDetailsSortie().isEmpty()) {
            for (MouvementSortieDetail detail : mvtSortie.getDetailsSortie()) {
                updateInventoryAndCreateMovement(detail, mvtSortie);
            }
        }

        logger.debug("Entité MouvementSortie après la sauvegarde avec ID : {}", mvtSortie.getId());
        if (mvtSortie.getId() == null) {
            logger.error("L'ID de MouvementSortie est null après la sauvegarde.");
            throw new IllegalStateException("L'ID de MouvementSortie ne peut pas être null après la sauvegarde.");
        }

        MouvementSortieDto savedMouvementSortieDto = mouvementSortieMapper.toDto(mvtSortie);
        logger.info("Fin de la méthode SaveMvtSortie avec savedMouvementSortieDto : {}", savedMouvementSortieDto);
        return savedMouvementSortieDto;
    }

    private void updateInventoryAndCreateMovement(MouvementSortieDetail detail, MouvementSortie mvtSortie) {
        logger.debug("Mise à jour de l'inventaire et création d'un mouvement pour l'article ID : {}", detail.getArticle().getIdArticle());
        // Logique d'inventaire et de création de mouvement
        final Long articleId = detail.getArticle().getIdArticle();
        final Integer entrepotId = mvtSortie.getEntrepot().getIdEntre();
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(articleId, entrepotId)
                .orElseThrow(() -> new RuntimeException("Stock non trouvé pour Article ID: " + articleId + " et Entrepot ID: " + entrepotId));
        if (inventaire.getQuantiteConforme() < detail.getQuantite()) {
            throw new RuntimeException("Quantité insuffisante en inventaire pour l'article ID: " + articleId);
        }
        inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() - detail.getQuantite());
        inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() + detail.getQuantite());
        inventaireRepository.save(inventaire);
        Mouvement mouvement = new Mouvement();
        mouvement.setInventaire(inventaire);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(-detail.getQuantite());
        mouvement.setCondition(Etat.NON_CONFORME.name());
        mouvement.setType(TypeMouvement.SORTIE);
        mouvement.setReference(mvtSortie.getReference());
        mouvementRepository.save(mouvement);
    }


    @Override
    public MouvementSortieDto getMvtSortieById(Long id) {
        MouvementSortie mouvementSortie = mouvementSortieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mouvement de sortie non trouvée avec l'id: " + id));
        return mouvementSortieMapper.toDto(mouvementSortie);
    }
    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int countToday = mouvementSortieRepository.countMvtSortie(LocalDate.now()) + 1;
        logger.info("Nombre de mouvements de sortie pour aujourd'hui (avant incrémentation) : {}", countToday);
        return "MS" + datePart + String.format("%04d", countToday);
    }
    @Override
    public List<MouvementSortieDto> getAllMvtSorties() {
        List<MouvementSortie> mvtSorties = mouvementSortieRepository.findAll();
        return mvtSorties.stream().map(mouvementSortieMapper::toDto).toList();
    }

    @Override
    public Optional<MouvementSortieDto>  findByReference(String reference) {
        return mouvementSortieRepository.findByReference(reference)
                .map(mouvementSortieMapper::toDto);
    }


}