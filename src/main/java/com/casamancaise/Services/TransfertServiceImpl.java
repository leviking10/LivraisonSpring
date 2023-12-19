package com.casamancaise.services;

import com.casamancaise.dao.*;
import com.casamancaise.dto.*;
import com.casamancaise.entities.*;
import com.casamancaise.mapping.*;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class TransfertServiceImpl implements TransfertService {
    private static final Logger logger = LoggerFactory.getLogger(TransfertServiceImpl.class);

    @Autowired
    private TransfertRepository transfertRepository;
    @Autowired
    private TransfertMapper transfertMapper;
    @Autowired
    private TransfertDetailsMapper transfertDetailsMapper;
    @Autowired
    private MouvementRepository mouvementRepository;
    @Autowired
    private InventaireRepository inventaireRepository;
    @Autowired
    private EntrepotRepository entrepotRepository;

    @Override
    public TransfertDto saveTransfert(TransfertDto transfertDto) {
        logger.info("Début de la méthode saveTransfert avec transfertDto: {}", transfertDto);
        LocalDate today = LocalDate.now();

        // Convertir DTO en entité
        Transfert transfert = transfertMapper.toEntity(transfertDto);
        transfert.setReference(generateReference());
        transfert.setTransferDate(today);

        // Associer les détails de transfert à l'entité Transfert
        if (transfertDto.getTransferDetails() != null && !transfertDto.getTransferDetails().isEmpty()) {
            transfert.getTransferDetails().clear(); // Nettoyer les anciens détails si nécessaire
            for (TransferDetailsDto detailDto : transfertDto.getTransferDetails()) {
                logger.debug("Traitement du TransferDetailsDto : {}", detailDto);
                TransferDetails detail = transfertDetailsMapper.toEntity(detailDto);
                detail.setTransfert(transfert); // Associer chaque détail au transfert
                transfert.getTransferDetails().add(detail);
            }
        }

        // Sauvegarder l'entité de transfert avec tous ses détails
        transfert = transfertRepository.save(transfert);

        // Gérer l'inventaire et les mouvements en fonction de l'état du transfert
        if (transfert.getEtat() == EtatTransfert.EN_COURS) {
            adjustSourceInventoryAndRecordMovement(transfert, TypeMouvement.SORTIE);
        } else if (transfert.getEtat() == EtatTransfert.TERMINE && transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            adjustDestinationInventoryAndRecordMovement(transfert, TypeMouvement.ENTREE);
        }

        logger.debug("Entité Transfert après la sauvegarde avec ID : {}", transfert.getId());
        if (transfert.getId() == null) {
            logger.error("L'ID de Transfert est null après la sauvegarde.");
            throw new IllegalStateException("L'ID de Transfert ne peut pas être null après la sauvegarde.");
        }

        TransfertDto savedTransfertDto = transfertMapper.toDto(transfert);
        logger.info("Fin de la méthode saveTransfert avec savedTransfertDto : {}", savedTransfertDto);
        return savedTransfertDto;
    }

    @Override
    public TransfertDto getTransfertById(Long id) {
        Transfert transfert = transfertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfert non trouvé avec l'id: " + id));
        return transfertMapper.toDto(transfert);
    }

    @Override
    public List<TransfertDto> getAllTransferts() {
        return transfertRepository.findAll().stream()
                .map(transfertMapper::toDto)
                .toList();
    }

    @Override
    public void deleteTransfert(Long id) {
        transfertRepository.deleteById(id);
    }

    @Override
    public Optional<TransfertDto> findByReference(String reference) {
        return transfertRepository.findByReference(reference)
                .map(transfertMapper::toDto);
    }

    private void adjustDestinationInventoryAndRecordMovement(Transfert transfert, TypeMouvement typeMouvement) {
        if (transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            Entrepot entrepotDestinataire = entrepotRepository.findById(transfert.getDestinataireId())
                    .orElseThrow(() -> new RuntimeException("Entrepôt destinataire non trouvé avec l'ID: " + transfert.getDestinataireId()));

            for (TransferDetails detail : transfert.getTransferDetails()) {
                int totalQuantite = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
                Inventaire inventaireDest = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                                detail.getArticle().getIdArticle(), entrepotDestinataire.getIdEntre())
                        .orElseGet(() -> new Inventaire(null, entrepotDestinataire, detail.getArticle(), 0, 0));

                inventaireDest.setQuantiteConforme(inventaireDest.getQuantiteConforme() + totalQuantite);
                inventaireRepository.save(inventaireDest);

                createMouvement(detail, transfert, totalQuantite, TypeMouvement.ENTREE);
            }
        }
    }


    private void adjustSourceInventoryAndRecordMovement(Transfert transfert, TypeMouvement typeMouvement) {
        for (TransferDetails detail : transfert.getTransferDetails()) {
            int totalQuantite = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
            Inventaire inventaireSource = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                            detail.getArticle().getIdArticle(), transfert.getFromEntrepot().getIdEntre())
                    .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: " + detail.getArticle().getIdArticle()));

            inventaireSource.setQuantiteConforme(inventaireSource.getQuantiteConforme() - totalQuantite);
            inventaireRepository.save(inventaireSource);

            createMouvement(detail, transfert, -totalQuantite, TypeMouvement.SORTIE);
        }
    }

    private void createMouvement(TransferDetails detail, Transfert transfert, int quantityChange, TypeMouvement typeMouvement) {
        Mouvement mouvement = new Mouvement();
        mouvement.setInventaire(findOrCreateInventaire(detail.getArticle(), (typeMouvement == TypeMouvement.SORTIE) ? transfert.getFromEntrepot() : getEntrepotById(transfert.getDestinataireId())));
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(quantityChange);
        mouvement.setCondition("CONFORME");
        mouvement.setType(typeMouvement);
        mouvement.setReference(transfert.getReference());
        mouvementRepository.save(mouvement);
    }
    private Entrepot getEntrepotById(Integer entrepotId) {
        return entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new RuntimeException("Entrepôt non trouvé avec l'ID: " + entrepotId));
    }


    private Inventaire findOrCreateInventaire(Article article, Entrepot entrepot) {
        return inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(article.getIdArticle(), entrepot.getIdEntre())
                .orElseGet(() -> new Inventaire(null, entrepot, article, 0, 0));
    }
    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        logger.info("Nombre de transferts pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = transfertRepository.countTransfert() + 1;
        return "TR" + datePart + String.format("%04d", count);
    }
}
