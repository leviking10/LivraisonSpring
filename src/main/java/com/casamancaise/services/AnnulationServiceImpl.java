package com.casamancaise.services;

import com.casamancaise.dao.AnnulationRepository;
import com.casamancaise.dao.InventaireRepository;
import com.casamancaise.dao.MouvementRepository;
import com.casamancaise.dao.ReceptionStockRepository;
import com.casamancaise.dto.AnnulationDto;
import com.casamancaise.dto.ReceptionStockDto;
import com.casamancaise.entities.*;
import com.casamancaise.mapping.AnnulationMapper;
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

@Service
@Transactional
public class AnnulationServiceImpl implements AnnulationService {
    @Autowired
    private AnnulationRepository annulationRepository;
    @Autowired
    private ReceptionStockRepository receptionStockRepository;
    @Autowired
    private InventaireRepository inventaireRepository;

    @Autowired
    private AnnulationMapper annulationMapper;
    @Autowired
    private MouvementRepository mouvementRepository;

    private static final Logger logger = LoggerFactory.getLogger(AnnulationServiceImpl.class);

    @Override
    public AnnulationDto createAnnulation(AnnulationDto annulationDto) {
        logger.info("Création d'une annulation avec les données : {}", annulationDto);
        if (annulationRepository.existsByRefReception(annulationDto.getRefReception())) {
            throw new IllegalStateException("Une annulation existe déjà pour cette référence: " + annulationDto.getRefReception());
        }
        // Définir la date du jour
        LocalDate today = LocalDate.now();
        Annulation annulation = annulationMapper.toEntity(annulationDto);
        String refAnnulation = genererRefAnnulation(); // Générer une seule référence pour l'annulation
        annulation.setRef(refAnnulation);
        annulation.setDateAnnulation(today);
        logger.info("Entité Annulation après la conversion du DTO : {}", annulation);

        ReceptionStock receptionStock = receptionStockRepository.findByReference(annulationDto.getRefReception())
                .orElseThrow(() -> new RuntimeException("Réception non trouvée avec la référence: " + annulationDto.getRefReception()));

        for (ReceptionDetail detail : receptionStock.getReceptionDetails()) {
            inverserMouvementEtMettreAJourInventaire(detail, receptionStock, refAnnulation); // Utiliser la référence générée
        }

        annulation = annulationRepository.save(annulation);
        logger.info("Annulation enregistrée avec succès : {}", annulation);

        return annulationMapper.toDto(annulation);
    }

    private String genererRefAnnulation() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = annulationRepository.countAnnulationForToday() + 1;
        return "ANRC" + datePart + String.format("%04d", count);
    }
    private void inverserMouvementEtMettreAJourInventaire(ReceptionDetail detail, ReceptionStock receptionStock, String refAnnulation) {
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(), receptionStock.getEntrepot().getIdEntre())
                .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: "
                        + detail.getArticle().getIdArticle() + " et entrepôt: "
                        + receptionStock.getEntrepot().getIdEntre()));

        if (detail.getEtat() == Etat.CONFORME) {
            inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() - detail.getQuantity());
        } else {
            inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() - detail.getQuantity());
        }
        inventaireRepository.save(inventaire);
        Mouvement mouvementInverse = new Mouvement();
        mouvementInverse.setInventaire(inventaire);
        mouvementInverse.setDateMouvement(LocalDateTime.now());
        mouvementInverse.setQuantiteChange(-detail.getQuantity());
        mouvementInverse.setCondition(detail.getEtat().name());
        mouvementInverse.setType(TypeMouvement.SORTIE);
        mouvementInverse.setReference(refAnnulation); // Utiliser la référence d'annulation générée
        mouvementRepository.save(mouvementInverse);
    }

    @Override
    public AnnulationDto getAnnulationById(Long id) {
        Annulation annulation = annulationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annulation non trouvée avec l'id: " + id));
        return annulationMapper.toDto(annulation);
    }
    @Override
    public List<AnnulationDto> getAllAnnulations() {
        return annulationRepository.findAll().stream()
                .map(annulationMapper::toDto)
                .toList();
    }

    @Override
    public Optional<AnnulationDto> findByRef(String reference) {
        return annulationRepository.findByRef(reference)
                .map(annulationMapper::toDto);
    }

}