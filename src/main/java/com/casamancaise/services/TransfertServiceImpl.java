package com.casamancaise.services;

import com.casamancaise.dao.*;
import com.casamancaise.dto.TransferDetailsDto;
import com.casamancaise.dto.TransfertDto;
import com.casamancaise.entities.*;
import com.casamancaise.enums.EtatTransfert;
import com.casamancaise.enums.TypeDestinataire;
import com.casamancaise.enums.TypeMouvement;
import com.casamancaise.mapping.TransfertDetailsMapper;
import com.casamancaise.mapping.TransfertMapper;
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
public class TransfertServiceImpl implements TransfertService {
    private final TransfertRepository transfertRepository;
    private final TransfertMapper transfertMapper;
    private final TransfertDetailsMapper transfertDetailsMapper;
    private final MouvementRepository mouvementRepository;
    private final ClientRepository clientRepository;
    private final InventaireRepository inventaireRepository;
    private final EntrepotRepository entrepotRepository;
    private AnnulationRepository annulationRepository;

    public TransfertServiceImpl(TransfertRepository transfertRepository, TransfertMapper transfertMapper, TransfertDetailsMapper transfertDetailsMapper, MouvementRepository mouvementRepository, ClientRepository clientRepository, InventaireRepository inventaireRepository, EntrepotRepository entrepotRepository,AnnulationRepository annulationRepository) {
        this.transfertRepository = transfertRepository;
        this.transfertMapper = transfertMapper;
        this.transfertDetailsMapper = transfertDetailsMapper;
        this.mouvementRepository = mouvementRepository;
        this.clientRepository = clientRepository;
        this.inventaireRepository = inventaireRepository;
        this.entrepotRepository = entrepotRepository;
        this.annulationRepository = annulationRepository;
    }
    @Override
    public TransfertDto saveTransfert(TransfertDto transfertDto) {
        log.info("Début de la méthode saveTransfert avec transfertDto: {}", transfertDto);
        LocalDate today = LocalDate.now();

        // Vérifier l'existence du destinataire avant de continuer
        verifierExistenceDestinataire(transfertDto.getTypeDestinataire(), transfertDto.getDestinataireId());

        // Convertir DTO en entité
        Transfert transfert = transfertMapper.toEntity(transfertDto);
        String transfertReference = generateReference();
        transfert.setReference(transfertReference);
        transfert.setTransferDate(today);
        transfert.setEtat(EtatTransfert.EN_COURS);
        transfert.setDeleted(false);
        // Associer les détails de transfert à l'entité Transfert et gérer l'inventaire
        transfert.getTransferDetails().clear();
        for (TransferDetailsDto detailDto : transfertDto.getTransferDetails()) {
            log.debug("Traitement du TransferDetailsDto: {}", detailDto);
            TransferDetails detail = transfertDetailsMapper.toEntity(detailDto);
            detail.setTransfert(transfert);
            transfert.getTransferDetails().add(detail);

            // Mise à jour de l'inventaire et création de mouvements pour chaque détail
            updateInventory(detail, transfert, false);
            createMovement(detail, transfert, TypeMouvement.SORTIE, transfertReference);
        }
        // Sauvegarder l'entité de transfert avec tous ses détails
        transfert = transfertRepository.save(transfert);
        log.debug("Entité Transfert après la sauvegarde avec ID: {}", transfert.getId());

        TransfertDto savedTransfertDto = transfertMapper.toDto(transfert);
        log.info("Fin de la méthode saveTransfert avec savedTransfertDto: {}", savedTransfertDto);
        return savedTransfertDto;
    }

    private void verifierExistenceDestinataire(TypeDestinataire typeDestinataire, Integer destinataireId) throws RuntimeException {
        try {
            if (typeDestinataire == TypeDestinataire.ENTREPOT) {
                if (!entrepotRepository.existsById(destinataireId)) {
                    throw new EntityNotFoundException("Entrepôt destinataire non trouvé avec l'ID: " + destinataireId);
                }
            } else if (typeDestinataire == TypeDestinataire.CLIENT) {
                List<Client> grossistes = clientRepository.findClientsByGrossiste();
                boolean isGrossiste = grossistes.stream().anyMatch(client -> client.getId().equals(Long.valueOf(destinataireId)));
                if (!isGrossiste) {
                    throw new EntityNotFoundException("Client destinataire n'est pas un grossiste avec l'ID: " + destinataireId);
                }
            } else {
                throw new IllegalArgumentException("Type de destinataire inconnu: " + typeDestinataire);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du destinataire: Type: {}, ID: {}, Erreur: {}", typeDestinataire, destinataireId, e.getMessage());
            throw e; // Relancer l'exception pour la gestion ultérieure
        }
    }
    private void createMovement(TransferDetails detail, Transfert transfert, TypeMouvement typeMouvement, String reference) {
        Mouvement mouvement = new Mouvement();
        Inventaire inventaire;

        // Déterminer l'inventaire en fonction du type de destinataire et du type de mouvement
        if (typeMouvement == TypeMouvement.ENTREE && transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            Entrepot entrepotDestinataire = getEntrepotById(transfert.getDestinataireId());
            inventaire = findOrCreateInventaire(detail.getArticle(), entrepotDestinataire);
        } else { // Pour les mouvements de sortie (y compris vers les clients)
            inventaire = findOrCreateInventaire(detail.getArticle(), transfert.getFromEntrepot());
        }

        // Calculer la quantité totale (quantité principale plus bonus)
        int totalQuantity = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
        int quantityChange = (typeMouvement == TypeMouvement.SORTIE) ? -totalQuantity : totalQuantity;

        // Configurer et enregistrer le mouvement
        mouvement.setInventaire(inventaire);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(quantityChange);
        mouvement.setCondition("CONFORME");
        mouvement.setType(typeMouvement);
        mouvement.setReference(reference);
        mouvementRepository.save(mouvement);
    }
    private Inventaire getOrCreateInventory(TransferDetails detail, Transfert transfert) {
        return inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(), transfert.getFromEntrepot().getIdEntre())
                .orElseGet(() -> {
                    Inventaire nouveauInventaire = new Inventaire();
                    nouveauInventaire.setEntrepot(transfert.getFromEntrepot());
                    nouveauInventaire.setArticle(detail.getArticle());
                    nouveauInventaire.setQuantiteConforme(0);
                    nouveauInventaire.setQuantiteNonConforme(0);
                    return inventaireRepository.save(nouveauInventaire);
                });
    }
    private void updateInventory(TransferDetails detail, Transfert transfert, boolean isCancellation) {
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(), transfert.getFromEntrepot().getIdEntre())
                .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: "
                        + detail.getArticle().getIdArticle() + " et entrepôt: "
                        + transfert.getFromEntrepot().getIdEntre()));

        // Ajouter la quantité bonus à la quantité principale
        int totalQuantity = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
        int quantityChange = isCancellation ? -totalQuantity : totalQuantity;

        // Mettre à jour l'inventaire
        inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() - quantityChange);
        inventaireRepository.save(inventaire);
    }

    @Override
    public TransfertDto updateTransfertDestinataire(Long id, TypeDestinataire nouveauTypeDestinataire, Integer nouveauDestinataireId) {
        log.info("Mise à jour du destinataire pour le transfert ID: {}", id);
        Transfert transfert = transfertRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Transfert non trouvé avec l'id: {}", id);
                    return new RuntimeException("Transfert non trouvé avec l'id: " + id);
                });

        if (transfert.getEtat() != EtatTransfert.EN_COURS) {
            log.warn("Tentative de modification du destinataire pour un transfert non en cours. ID: {}", id);
            throw new EntityNotFoundException("La modification du destinataire n'est autorisée que pour les transferts en cours.");
        }

        log.info("Vérification de l'existence et de la validité du nouveau destinataire. ID: {}, Type: {}", nouveauDestinataireId, nouveauTypeDestinataire);
        verifierExistenceDestinataire(nouveauTypeDestinataire, nouveauDestinataireId);

        log.info("Mise à jour du type et de l'ID du destinataire. Nouveau Type: {}, Nouvel ID: {}", nouveauTypeDestinataire, nouveauDestinataireId);
        transfert.setTypeDestinataire(nouveauTypeDestinataire);
        transfert.setDestinataireId(nouveauDestinataireId);

        log.info("Sauvegarde des modifications pour le transfert ID: {}", id);
        transfert = transfertRepository.save(transfert);
        TransfertDto savedTransfertDto = transfertMapper.toDto(transfert);
        log.info("Mise à jour du destinataire terminée pour le transfert ID: {}", id);
        return savedTransfertDto;
    }

    @Override
    public void annulerTransfert(Long id, String raison) {
        Transfert transfert = transfertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfert non trouvé avec l'id: " + id));
        if (transfert.isDeleted()) {
            throw new IllegalStateException("Ce transfert  a déjà été annulé.");
        }
        if (!transfert.getEtat().equals(EtatTransfert.EN_COURS)) {
            throw new IllegalStateException("Seuls les transferts qui sont en cours peuvent être annulés.");
        }
        transfert.setDeleted(true);
        transfertRepository.save(transfert);
        String refAnnulation = genererRefAnnulation();
        Annulation annulation = createAnnulation(transfert, refAnnulation, raison);
        annulationRepository.save(annulation);

        for (TransferDetails detail : transfert.getTransferDetails()) {
            updateInventoryAndCreateReverseMovement(detail, transfert);
        }
    }

    private void updateInventoryAndCreateReverseMovement(TransferDetails detail, Transfert transfert) {
        Inventaire inventaire = getOrCreateInventory(detail, transfert);
        // Inclure les bonus dans la quantité inversée
        int totalQuantity = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
        inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + totalQuantity); // Inverse la diminution initiale
        inventaireRepository.save(inventaire);
        createReverseMovement(detail, transfert);
    }

    private void createReverseMovement(TransferDetails detail, Transfert transfert) {
        Mouvement mouvementInverse = new Mouvement();
        mouvementInverse.setInventaire(getOrCreateInventory(detail, transfert));
        mouvementInverse.setDateMouvement(LocalDateTime.now());
        int totalQuantity = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
        mouvementInverse.setQuantiteChange(totalQuantity);
        mouvementInverse.setCondition("CONFORME");
        mouvementInverse.setType(TypeMouvement.ENTREE);
        mouvementInverse.setReference(transfert.getReference());
        mouvementRepository.save(mouvementInverse);
    }

    private Annulation createAnnulation(Transfert transfert, String refAnnulation, String raison) {
        Annulation annulation = new Annulation();
        annulation.setRef(refAnnulation);
        annulation.setRefOperation(transfert.getReference());
        annulation.setDateAnnulation(LocalDate.now());
        annulation.setRaison(raison);
        return annulation;
    }

    @Override
    public TransfertDto recevoirTransfert(Long id, LocalDate dateLivraison) {
        log.info("Début de la réception du transfert pour l'ID de transfert: {}", id);

        Transfert transfert = transfertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Impossible de faire cette réception car ce transfert n'existe pas."));
        if (!transfert.getEtat().equals(EtatTransfert.EN_COURS)) {
            log.warn("Tentative de réception d'un transfert qui n'est pas en cours. ID: {}", id);
            throw new IllegalStateException("Seuls les transferts en cours peuvent être reçus.");
        }
        transfert.setEtat(EtatTransfert.TERMINE);
        transfert.setReceptionDate(dateLivraison);
        if (transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            adjustDestinationInventory(transfert);
            log.info("Ajustement de l'inventaire effectué pour l'entrepôt destinataire. ID de transfert: {}", id);
        }
        transfert = transfertRepository.save(transfert);
        log.info("Transfert reçu avec succès. ID de transfert: {}", id);
        return transfertMapper.toDto(transfert);
    }

    private void adjustDestinationInventory(Transfert transfert) {
        if (transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            Entrepot entrepotDestinataire = entrepotRepository.findById(transfert.getDestinataireId())
                    .orElseThrow(() -> new RuntimeException("Entrepôt destinataire non trouvé avec l'ID: " + transfert.getDestinataireId()));
            for (TransferDetails detail : transfert.getTransferDetails()) {
                int totalQuantite = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
                Inventaire inventaireDest = findOrCreateInventaire(detail.getArticle(), entrepotDestinataire);
                inventaireDest.setQuantiteConforme(inventaireDest.getQuantiteConforme() + totalQuantite);
                inventaireRepository.save(inventaireDest);
                createMovement(detail, transfert, TypeMouvement.ENTREE, transfert.getReference());
            }
        }
    }

    @Override
    public TransfertDto getTransfertById(Long id) {
        Transfert transfert = transfertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Le transfert non trouvé avec l'id: " + id));
        return transfertMapper.toDto(transfert);
    }

    @Override
    public List<TransfertDto> getAllTransferts() {
        return transfertRepository.findAll().stream()
                .map(transfertMapper::toDto)
                .toList();
    }

    @Override
    public Optional<TransfertDto> findByReference(String reference) {
        return transfertRepository.findByReference(reference)
                .map(transfertMapper::toDto);
    }

    private Entrepot getEntrepotById(Integer entrepotId) {
        return entrepotRepository.findById(entrepotId)
                .orElseThrow(() -> new RuntimeException("Entrepôt non trouvé avec l'ID: " + entrepotId));
    }

    private Inventaire findOrCreateInventaire(Article article, Entrepot entrepot) {
        return inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(article.getIdArticle(), entrepot.getIdEntre())
                .orElseGet(() -> new Inventaire(null, entrepot, article, 0, 0));
    }


    private String genererRefAnnulation() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = annulationRepository.countAnnulationForToday() + 1;
        return "AN" + datePart + String.format("%04d", count);
    }

    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("Nombre de transferts pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = transfertRepository.countTransfert() + 1;
        return "TR" + datePart + String.format("%04d", count);
    }
}
