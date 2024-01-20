package com.casamancaise.services;

import com.casamancaise.dao.*;
import com.casamancaise.dto.DetailRetourDto;
import com.casamancaise.dto.RetourDto;
import com.casamancaise.entities.*;
import com.casamancaise.enums.*;
import com.casamancaise.mapping.DetailRetourMapper;
import com.casamancaise.mapping.RetourMapper;
import jakarta.persistence.EntityNotFoundException;
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
public class RetourServiceImpl implements RetourService {
    private final RetourRepository retourRepository;
    private final RetourMapper retourMapper;
    private final TransfertRepository transfertRepository;
    private final VenteRepository venteRepository;
    private final DetailRetourMapper detailRetourMapper;
    private final MouvementRepository mouvementRepository;
    private final InventaireRepository inventaireRepository;
    private final AnnulationRepository annulationRepository;
    private final DetailRetourRepository detailRetourRepository;
    public RetourServiceImpl(RetourRepository retourRepository, RetourMapper retourMapper, TransfertRepository transfertRepository, VenteRepository venteRepository, DetailRetourMapper detailRetourMapper, MouvementRepository mouvementRepository, InventaireRepository inventaireRepository, AnnulationRepository annulationRepository, DetailRetourRepository detailRetourRepository) {
        this.retourRepository = retourRepository;
        this.retourMapper = retourMapper;
        this.transfertRepository = transfertRepository;
        this.venteRepository = venteRepository;
        this.detailRetourMapper = detailRetourMapper;
        this.mouvementRepository = mouvementRepository;
        this.inventaireRepository = inventaireRepository;
        this.annulationRepository = annulationRepository;
        this.detailRetourRepository = detailRetourRepository;
    }

    @Override
    public RetourDto saveRetour(RetourDto retourDto) {
        log.info("Début de la méthode saveRetour avec retourDto: {}", retourDto);
        LocalDate today = LocalDate.now();

        Retour retour = retourMapper.toEntity(retourDto);
        retour.setDateRetour(today);
        retour.setReference(generateReference());
        retour.setDeleted(false);

        if (retourDto.getTypeRetour() == TypeRetour.VENTE) {
            Vente vente = venteRepository.findById(retourDto.getOperationId())
                    .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée pour l'ID: " + retourDto.getOperationId()));
            if (vente.isDeleted()) {
                throw new IllegalStateException("La vente associée  a été supprimée.");
            }
            if (vente.getStatut() != StatutVente.LIVREE) {
                throw new IllegalStateException("La vente  n'est pas terminée.");
            }
            traiterRetourVente(retourDto);
        } else if (retourDto.getTypeRetour() == TypeRetour.TRANSFERT) {
            Transfert transfert = transfertRepository.findById(retourDto.getOperationId())
                    .orElseThrow(() -> new EntityNotFoundException("Transfert non trouvé."));
            if (transfert.isDeleted()) {
                throw new IllegalStateException("Le transfert associé a été supprimé.");
            }
            if (transfert.getEtat() != EtatTransfert.TERMINE) {
                throw new IllegalStateException("Le transfert n'est pas terminé.");
            }
            traiterRetourTransfert(retourDto);
        }

        verifierQuantitesRetournees(retourDto);
        traiterDetailsRetour(retour, retourDto.getDetailsRetours());
        majInventaire(retourDto);
        creerMouvements(retour);

        retour = retourRepository.save(retour);
        if (retour.getId() == null) {
            log.error("L'ID de Retour est null après la sauvegarde.");
            throw new EntityNotFoundException("L'ID de Retour ne peut pas être null après la sauvegarde.");
        }

        return retourMapper.toDto(retour);
    }


    private void verifierQuantitesRetournees(RetourDto retourDto) {
        for (DetailRetourDto detailRetourDto : retourDto.getDetailsRetours()) {
            Long operationId = retourDto.getOperationId();
            Long articleId = detailRetourDto.getArticleId(); // Utilisez l'ID de l'article du DTO

            // Vérifier l'existence de l'article dans l'opération
            boolean articleExisteDansOperation = (retourDto.getTypeRetour() == TypeRetour.VENTE)
                    ? checkArticleExistenceInVente(articleId, operationId)
                    : checkArticleExistenceInTransfert(articleId, operationId);

            if (!articleExisteDansOperation) {
                throw new EntityNotFoundException("L'article avec ID " + articleId + " n'est pas présent dans l'opération spécifiée.");
            }

            // Si l'article existe, procéder à la vérification de la quantité
            int quantiteTotaleOperation = (retourDto.getTypeRetour() == TypeRetour.VENTE)
                    ? getQuantiteTotaleVente(articleId, operationId)
                    : getQuantiteTotaleTransfert(articleId, operationId);

            int quantiteDejaRetournee = getQuantiteDejaRetournee(articleId, operationId);
            int quantiteRetourneeActuelle = detailRetourDto.getQuantiteRetournee();

            if (quantiteRetourneeActuelle + quantiteDejaRetournee > quantiteTotaleOperation) {
                throw new EntityNotFoundException("La quantité totale retournée pour l'article avec ID " + articleId
                        + " dépasse la quantité de l'opération.");
            }
        }
    }

    private boolean checkArticleExistenceInVente(Long articleId, Long venteId) {
        return venteRepository.findById(venteId)
                .map(vente -> vente.getDetailVentes().stream()
                        .anyMatch(detailVente -> detailVente.getArticle().getIdArticle().equals(articleId)))
                .orElse(false);
    }

    private boolean checkArticleExistenceInTransfert(Long articleId, Long transfertId) {
        return transfertRepository.findById(transfertId)
                .map(transfert -> transfert.getTransferDetails().stream()
                        .anyMatch(transferDetail -> transferDetail.getArticle().getIdArticle().equals(articleId)))
                .orElse(false);
    }

    private int getQuantiteTotaleVente(Long articleId, Long venteId) {
        return venteRepository.findById(venteId)
                .flatMap(vente -> vente.getDetailVentes().stream()
                        .filter(detail -> detail.getArticle().getIdArticle().equals(articleId))
                        .findFirst())
                .map(detailVente -> detailVente.getQuantity() + (detailVente.getBonus() != null ? detailVente.getBonus() : 0))
                .orElse(0);
    }

    private int getQuantiteTotaleTransfert(Long articleId, Long transfertId) {
        return transfertRepository.findById(transfertId)
                .flatMap(transfert -> transfert.getTransferDetails().stream()
                        .filter(detail -> detail.getArticle().getIdArticle().equals(articleId))
                        .findFirst())
                .map(transferDetail -> transferDetail.getQuantite() + (transferDetail.getBonus() != null ? transferDetail.getBonus() : 0))
                .orElse(0);
    }

    private int getQuantiteDejaRetournee(Long articleId, Long operationId) {
        return detailRetourRepository.findAllByArticleIdAndOperationId(articleId, operationId).stream()
                .mapToInt(DetailRetour::getQuantiteRetournee)
                .sum();
    }


    @Override
    public void annulerRetour(Long id, String raison) {
        Retour retour = retourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retour non trouvé avec l'id: " + id));
        if (retour.isDeleted()) {
            throw new IllegalStateException("Ce mouvement de retour a déjà été annulé.");
        }
        // Vérification de l'état de la vente ou du transfert associé
        if (retour.getTypeRetour() == TypeRetour.VENTE) {
            Vente vente = venteRepository.findById(retour.getOperationId())
                    .orElseThrow(() -> new RuntimeException("Vente non trouvée avec l'id: " + retour.getOperationId()));
            if (vente.isDeleted()) {
                throw new IllegalStateException("Impossible d'annuler le retour car la vente associée a été supprimée.");
            }
            if (vente.getStatut() != StatutVente.LIVREE) {
                throw new IllegalStateException("Impossible d'annuler le retour car la vente associée est toujours en cours de traitement.");
            }
        } else if (retour.getTypeRetour() == TypeRetour.TRANSFERT) {
            Transfert transfert = transfertRepository.findById(retour.getOperationId())
                    .orElseThrow(() -> new RuntimeException("Transfert non trouvé avec l'id: " + retour.getOperationId()));
            if (transfert.isDeleted()) {
                throw new IllegalStateException("Impossible d'annuler le retour car le transfert associé a été supprimé.");
            }
            if (transfert.getEtat() != EtatTransfert.TERMINE) {
                throw new IllegalStateException("Impossible d'annuler le retour car le transfert associé est toujours en cours de traitement.");
            }
        }
        retour.setDeleted(true);
        retourRepository.save(retour);
        String refAnnulation = genererRefAnnulation();
        Annulation annulation = createAnnulation(retour, refAnnulation, raison);
        annulationRepository.save(annulation);
        for (DetailRetour detail : retour.getDetailsRetours()) {
            updateInventoryAndCreateReverseMovement(detail, retour);
        }
    }
    private void updateInventoryAndCreateReverseMovement(DetailRetour detail, Retour retour) {
        Inventaire inventaire = getOrCreateInventory(detail, retour);
        int totalQuantity = detail.getQuantiteRetournee();
        inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() - totalQuantity); // Soustraire au lieu d'ajouter
        inventaireRepository.save(inventaire);

        createReverseMovement(detail, retour); // Créer un mouvement de sortie
    }

    private Inventaire getOrCreateInventory(DetailRetour detail, Retour retour) {
        return inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(), retour.getEntrepot().getIdEntre())
                .orElseGet(() -> {
                    Inventaire nouveauInventaire = new Inventaire();
                    nouveauInventaire.setEntrepot(retour.getEntrepot());
                    nouveauInventaire.setArticle(detail.getArticle());
                    nouveauInventaire.setQuantiteConforme(0);
                    nouveauInventaire.setQuantiteNonConforme(0);
                    return inventaireRepository.save(nouveauInventaire);
                });
    }
    private void createReverseMovement(DetailRetour detail, Retour retour) {
        Mouvement mouvementInverse = new Mouvement();
        mouvementInverse.setInventaire(getOrCreateInventory(detail, retour));
        mouvementInverse.setDateMouvement(LocalDateTime.now());
        int totalQuantity = detail.getQuantiteRetournee();
        mouvementInverse.setQuantiteChange(-totalQuantity); // Quantité négative pour une sortie
        mouvementInverse.setCondition(detail.getEtat().name());
        mouvementInverse.setType(TypeMouvement.SORTIE); // "SORTIE"
        mouvementInverse.setReference(retour.getReference());
        mouvementRepository.save(mouvementInverse);
    }

    private Annulation createAnnulation(Retour retour, String refAnnulation, String raison) {
        Annulation annulation = new Annulation();
        annulation.setRef(refAnnulation);
        annulation.setRefOperation(retour.getReference());
        annulation.setDateAnnulation(LocalDate.now());
        annulation.setRaison(raison);
        return annulation;
    }


    private void traiterRetourTransfert(RetourDto retourDto) throws RuntimeException {
        // Rechercher le transfert par son ID
        Transfert transfert = transfertRepository.findById(retourDto.getOperationId())
                .orElseThrow(() -> new EntityNotFoundException("Transfert non trouvé pour l'ID: " + retourDto.getOperationId()));
        // Vérifier si le type de destinataire est 'CLIENT'
        if (transfert.getTypeDestinataire() != TypeDestinataire.CLIENT)
            throw new EntityNotFoundException("Le transfert avec l'ID: " + retourDto.getOperationId() + " n'est pas destiné à un client");
        log.info("Traitement du retour pour le transfert destiné au client, ID du transfert: {}", transfert.getId());

    }

    private void traiterRetourVente(RetourDto retourDto) {
        Vente vente = venteRepository.findById(retourDto.getOperationId())
                .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée pour l'ID: " + retourDto.getOperationId()));
        log.info("Traitement du retour pour la vente, ID de la vente: {}", vente.getId());
    }

    private void traiterDetailsRetour(Retour retour, List<DetailRetourDto> detailsRetourDtos) {
        retour.getDetailsRetours().clear();
        for (DetailRetourDto detailDto : detailsRetourDtos) {
            DetailRetour detail = detailRetourMapper.toEntity(detailDto);
            detail.setRetour(retour);
            retour.getDetailsRetours().add(detail);
        }
    }

    //Mettons à jour d'etat du stock
    private void majInventaire(RetourDto retourDto) {
        for (DetailRetourDto detailDto : retourDto.getDetailsRetours()) {
            Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                            detailDto.getArticleId(), Math.toIntExact(retourDto.getEntrepotId()))
                    .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: " + detailDto.getArticleId() + " et entrepôt: " + retourDto.getEntrepotId()));

            int quantityChange = detailDto.getQuantiteRetournee();
            if (detailDto.getEtat() == Etat.CONFORME) {
                inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + quantityChange);
            } else {
                inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() + quantityChange);
            }
            inventaireRepository.save(inventaire);
        }
    }

    //Gérons la traçabilité en faisant aussi l'enregistrement sur la table mouvement
    private void creerMouvements(Retour retour) {
        for (DetailRetour detail : retour.getDetailsRetours()) {
            Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                            detail.getArticle().getIdArticle(), retour.getEntrepot().getIdEntre())
                    .orElseThrow(() -> new EntityNotFoundException("Inventaire non trouvé pour l'article ID: "
                            + detail.getArticle().getIdArticle() + " et entrepôt ID: " + retour.getEntrepot().getIdEntre()));
            Mouvement mouvement = new Mouvement();
            mouvement.setInventaire(inventaire);
            mouvement.setDateMouvement(LocalDateTime.now());
            mouvement.setQuantiteChange(detail.getQuantiteRetournee()); // Assurez-vous que cette méthode existe dans DetailRetour
            mouvement.setCondition(detail.getEtat().name()); // Assurez-vous que DetailRetour a une propriété 'etat'
            mouvement.setType(TypeMouvement.ENTREE);
            mouvement.setReference(retour.getReference()); // Utilise la référence de l'entité Retour
            mouvementRepository.save(mouvement);
        }
    }
    @Override
    public RetourDto getRetourById(Long id) {
        Retour retour = retourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Retour not found"));
        return retourMapper.toDto(retour);
    }

    @Override
    public void deleteRetour(Long id) {
        retourRepository.deleteById(id);
    }

    @Override
    public List<RetourDto> getAllRetours() {
        return retourRepository.findAll().stream()
                .map(retourMapper::toDto)
                .toList();
    }

    @Override
    public RetourDto updateRetour(Long id, RetourDto retourDto) {
        Retour existingRetour = retourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retour non trouvé pour l'ID: " + id));
        retourMapper.updateFromDto(retourDto, existingRetour);
        existingRetour = retourRepository.save(existingRetour);
        return retourMapper.toDto(existingRetour);
    }
    private String generateReference() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("Nombre de retour pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = retourRepository.countRetoursForToday() + 1;
        return "BR" + datePart + String.format("%04d", count);
    }
    private String genererRefAnnulation() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int count = annulationRepository.countAnnulationForToday() + 1;
        return "AN" + datePart + String.format("%04d", count);
    }
    @Override
    public Optional<RetourDto> findByReference(String reference) {
        return retourRepository.findByReference(reference)
                .map(retourMapper::toDto);
    }
}
