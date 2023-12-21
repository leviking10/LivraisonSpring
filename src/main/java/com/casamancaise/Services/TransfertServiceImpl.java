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
    private ClientRepository clientRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private InventaireRepository inventaireRepository;
    @Autowired
    private EntrepotRepository entrepotRepository;

    @Override
    public TransfertDto saveTransfert(TransfertDto transfertDto) {
        logger.info("Début de la méthode saveTransfert avec transfertDto: {}", transfertDto);
        LocalDate today = LocalDate.now();

        // Initialiser canalDistrib en fonction du type de destinataire
        Integer canalDistribId = null;
        if (transfertDto.getTypeDestinataire() == TypeDestinataire.CLIENT) {
            // Récupérer le canal de distribution du client
            ClientDto client = clientService.getClientById(Long.valueOf(transfertDto.getDestinataireId()));
            canalDistribId = client.getCanalDistribId();
        }

        // Vérifier l'existence du destinataire avant de continuer
        verifierExistenceDestinataire(transfertDto.getTypeDestinataire(), transfertDto.getDestinataireId(), canalDistribId);

        // Convertir DTO en entité
        Transfert transfert = transfertMapper.toEntity(transfertDto);
        transfert.setReference(generateReference());
        transfert.setTransferDate(today);
        transfert.setEtat(EtatTransfert.EN_COURS);
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
    public TransfertDto updateTransfertStatus(Long id, EtatTransfert etat) {
        Transfert transfert = transfertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfert non trouvé avec l'id: " + id));
        transfert.setEtat(etat);
        if (etat == EtatTransfert.TERMINE) {
            handleTransfertCompletion(transfert);
        }
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }

    private void handleTransfertCompletion(Transfert transfert) {
        // Si le transfert est terminé et le destinataire est un entrepôt, ajuster l'inventaire de l'entrepôt destinataire
        if (transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            adjustDestinationInventoryAndRecordMovement(transfert, TypeMouvement.ENTREE);
        }
    }
    @Override
    public TransfertDto recevoirTransfert(Long id, LocalDate dateLivraison) {
        logger.info("Début de la reception du transfert pour l'ID de transfert: {}", id);

        Transfert transfert = transfertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfert non trouvé avec l'id: " + id));

        if (!transfert.getEtat().equals(EtatTransfert.EN_COURS)) {
            logger.warn("Tentative de réception d'un transfert qui n'est en cours. ID: {}", id);
            throw new IllegalStateException("Seuls les transferts en cours peuvent être reçus.");
        }

        logger.info("Le transfert est en cours et prêt à être reçu. ID: {}", id);

        transfert.setEtat(EtatTransfert.TERMINE);
        transfert.setReceptionDate(dateLivraison);

        if (transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            adjustDestinationInventoryAndRecordMovement(transfert, TypeMouvement.ENTREE);
            logger.info("Ajustement de l'inventaire effectué pour l'entrepôt destinataire. ID de transfert: {}", id);
        }

        transfert = transfertRepository.save(transfert);

        logger.info("Transfert reçu avec succès. ID de transfert: {}", id);

        return transfertMapper.toDto(transfert);
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
    private void annulerAjustementsEtMouvements(Transfert transfert) {
        if (transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            annulerAjustementsInventaireEntrepotDestinataire(transfert);
        }
        annulerAjustementsInventaireEntrepotSource(transfert);
    }

    private void annulerAjustementsInventaireEntrepotDestinataire(Transfert transfert) {
        // Supposons que l'ajout à l'entrepôt destinataire a déjà été effectué
        for (TransferDetails detail : transfert.getTransferDetails()) {
            int totalQuantite = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
            Entrepot entrepotDestinataire = entrepotRepository.findById(transfert.getDestinataireId())
                    .orElseThrow(() -> new RuntimeException("Entrepôt non trouvé avec l'ID: " + transfert.getDestinataireId()));

            Inventaire inventaireDest = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                            detail.getArticle().getIdArticle(), entrepotDestinataire.getIdEntre())
                    .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: " + detail.getArticle().getIdArticle()));

            inventaireDest.setQuantiteConforme(inventaireDest.getQuantiteConforme() - totalQuantite);
            inventaireRepository.save(inventaireDest);

            createMouvement(detail, transfert, -totalQuantite, TypeMouvement.SORTIE);
        }
    }
    private void annulerAjustementsInventaireEntrepotSource(Transfert transfert) {
        // Réintégrer les articles dans l'inventaire de l'entrepôt source
        for (TransferDetails detail : transfert.getTransferDetails()) {
            int totalQuantite = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
            Inventaire inventaireSource = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                            detail.getArticle().getIdArticle(), transfert.getFromEntrepot().getIdEntre())
                    .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: " + detail.getArticle().getIdArticle()));

            inventaireSource.setQuantiteConforme(inventaireSource.getQuantiteConforme() + totalQuantite);
            inventaireRepository.save(inventaireSource);

            createMouvement(detail, transfert, totalQuantite, TypeMouvement.ENTREE);
        }
    }
    @Override
    public TransfertDto annulerTransfert(String reference) {
        Transfert transfert = transfertRepository.findByReference(reference)
                .orElseThrow(() -> new RuntimeException("Transfert non trouvé avec la référence: " + reference));
        if (transfert.getEtat() != EtatTransfert.EN_COURS) {
            throw new IllegalStateException("Seuls les transferts en cours peuvent être annulés.");
        }
        transfert.setEtat(EtatTransfert.ANNULE);
        annulerAjustementsEtMouvements(transfert);
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }
    private void adjustSourceInventoryAndRecordMovement(Transfert transfert, TypeMouvement typeMouvement) {
        for (TransferDetails detail : transfert.getTransferDetails()) {
            int totalQuantite = detail.getQuantite() + (detail.getBonus() != null ? detail.getBonus() : 0);
            Inventaire inventaireSource = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                            detail.getArticle().getIdArticle(), transfert.getFromEntrepot().getIdEntre())
                    .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: " + detail.getArticle().getIdArticle()));
            inventaireSource.setQuantiteConforme(inventaireSource.getQuantiteConforme() - totalQuantite);
            inventaireRepository.save(inventaireSource);
            createMouvement(detail, transfert, -totalQuantite, typeMouvement.SORTIE);
        }
    }
    private void createMouvement(TransferDetails detail, Transfert transfert, int quantityChange, TypeMouvement typeMouvement) {
        Mouvement mouvement = new Mouvement();
        // S'il s'agit d'un transfert vers un entrepôt, trouver ou créer l'inventaire pour l'entrepôt destinataire
        if (typeMouvement == TypeMouvement.ENTREE && transfert.getTypeDestinataire() == TypeDestinataire.ENTREPOT) {
            Entrepot entrepotDestinataire = getEntrepotById(transfert.getDestinataireId());
            mouvement.setInventaire(findOrCreateInventaire(detail.getArticle(), entrepotDestinataire));
        } else if (typeMouvement == TypeMouvement.SORTIE) { // Pour les mouvements de sortie (y compris vers les clients)
            mouvement.setInventaire(findOrCreateInventaire(detail.getArticle(), transfert.getFromEntrepot()));
        }
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
    @Override
    public TransfertDto updateTransfertDestinataire(Long id, TypeDestinataire nouveauTypeDestinataire, Integer nouveauDestinataireId) {
        Transfert transfert = transfertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfert non trouvé avec l'id: " + id));

        if (transfert.getEtat() != EtatTransfert.EN_COURS) {
            throw new IllegalStateException("La modification du destinataire n'est autorisée que pour les transferts en cours.");
        }

        // Initialiser canalDistrib en fonction du nouveau type de destinataire
        Integer canalDistribId = null;
        if (nouveauTypeDestinataire == TypeDestinataire.CLIENT) {
            ClientDto client = clientService.getClientById(Long.valueOf(nouveauDestinataireId));
            canalDistribId = client.getCanalDistribId();
        }

        verifierExistenceDestinataire(nouveauTypeDestinataire, nouveauDestinataireId, canalDistribId);

        transfert.setTypeDestinataire(nouveauTypeDestinataire);
        transfert.setDestinataireId(nouveauDestinataireId);
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }

    private void verifierExistenceDestinataire(TypeDestinataire typeDestinataire, Integer destinataireId, Integer canalDistribId) {
        if (typeDestinataire == TypeDestinataire.ENTREPOT) {
            if (!entrepotRepository.existsById(destinataireId)) {
                throw new RuntimeException("Entrepôt destinataire non trouvé avec l'ID: " + destinataireId);
            }
        } else if (typeDestinataire == TypeDestinataire.CLIENT) {
            if (canalDistribId != null) {
                boolean clientExiste = clientRepository.existsByIdAndCanalDistribId(Long.valueOf(destinataireId), canalDistribId);
                if (!clientExiste) {
                    throw new RuntimeException("Client destinataire non trouvé avec l'ID: " + destinataireId + " et canal de distribution ID: " + canalDistribId);
                }
            } else {
                if (!clientRepository.existsById(Long.valueOf(destinataireId))) {
                    throw new RuntimeException("Client destinataire non trouvé avec l'ID: " + destinataireId);
                }
            }
        } else {
            throw new IllegalArgumentException("Type de destinataire inconnu: " + typeDestinataire);
        }
    }
    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        logger.info("Nombre de transferts pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = transfertRepository.countTransfert() + 1;
        return "TR" + datePart + String.format("%04d", count);
    }
}
