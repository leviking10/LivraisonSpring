package com.casamancaise.services;

import com.casamancaise.dao.*;
import com.casamancaise.dto.DetailVenteDto;
import com.casamancaise.dto.VenteDto;
import com.casamancaise.entities.*;
import com.casamancaise.enums.Etat;
import com.casamancaise.enums.StatutVente;
import com.casamancaise.enums.TypeMouvement;
import com.casamancaise.mapping.DetailVenteMapper;
import com.casamancaise.mapping.VenteMapper;
import com.casamancaise.myexeptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class VenteServiceImpl implements VenteService {
    private static final Logger logger = LoggerFactory.getLogger(VenteServiceImpl.class);
    private final VenteRepository venteRepository;
    private final VenteMapper venteMapper;
    private final MouvementRepository mouvementRepository;
    private final InventaireRepository inventaireRepository;
    private final ClientRepository clientRepository;
    private final ArticleRepository articleRepository;
    private final DetailVenteMapper detailVenteMapper;
    private final AnnulationRepository annulationRepository;

    public VenteServiceImpl(VenteRepository venteRepository, VenteMapper venteMapper, MouvementRepository mouvementRepository, InventaireRepository inventaireRepository, AnnulationRepository annulationRepository, ClientRepository clientRepository, ArticleRepository articleRepository, DetailVenteMapper detailVenteMapper) {
        this.venteRepository = venteRepository;
        this.venteMapper = venteMapper;
        this.mouvementRepository = mouvementRepository;
        this.inventaireRepository = inventaireRepository;
        this.annulationRepository = annulationRepository;
        this.clientRepository = clientRepository;
        this.articleRepository = articleRepository;
        this.detailVenteMapper = detailVenteMapper;
    }
    @Override
    public VenteDto saveVente(VenteDto venteDto) {
        logger.info("Début de la méthode saveVente avec venteDto: {}", venteDto);
        LocalDate today = LocalDate.now();

        // Vérifier l'existence du client
        if (!clientRepository.existsById(venteDto.getClientId())) {
            throw new EntityNotFoundException("Ce Client avec l'ID: " + venteDto.getClientId() + " n'existe pas");
        }

        // Vérifier la disponibilité du stock avant de continuer
        if (!verifierDisponibiliteArticle(venteDto)) {
            logger.error("Vente non réalisée en raison d'un stock insuffisant.");
            throw new IllegalStateException("Stock insuffisant pour réaliser la vente.");
        }

        // Convertir DTO en entité
        Vente vente = venteMapper.toEntity(venteDto);
        vente.setReference(generateReference());
        vente.setDateVente(today);
        vente.setStatut(StatutVente.EN_COURS);
        vente.setDeleted(false);

        // Initialiser le poids total avec BigDecimal pour la précision de l'arrondi
        BigDecimal poidsTotal = BigDecimal.ZERO;

        // Traiter chaque détail de la vente
        vente.getDetailVentes().clear(); // Nettoyer les anciens détails si nécessaire
        for (DetailVenteDto detailDto : venteDto.getDetailVentes()) {
            DetailVente detail = detailVenteMapper.toEntity(detailDto);
            detail.setVente(vente); // Associer chaque détail à la vente
            vente.getDetailVentes().add(detail);

            // Vérifier et récupérer le tonnage de l'article
            if (detail.getArticle() == null || detail.getArticle().getIdArticle() == null) {
                logger.error("L'article pour DetailVente est null ou n'a pas d'ID valide.");
                throw new EntityNotFoundException("L'article pour DetailVente est manquant ou incorrect.");
            }

            Article article = articleRepository.findById(detail.getArticle().getIdArticle())
                    .orElseThrow(() -> new EntityNotFoundException("Article avec l'ID: " + detail.getArticle().getIdArticle() + " n'existe pas."));

            double pu = article.getTonage();
            logger.info("Tonnage récupéré pour l'article ID {}: {}", article.getIdArticle(), pu);

            // Utiliser BigDecimal pour le calcul du poids
            BigDecimal poidsUnitaire = BigDecimal.valueOf((pu));
            int quantiteTotale = detail.getQuantity() + (detail.getBonus() != null ? detail.getBonus() : 0);
            BigDecimal poidsPourDetail = poidsUnitaire.multiply(BigDecimal.valueOf(quantiteTotale));
            poidsTotal = poidsTotal.add(poidsPourDetail);

            // Mise à jour de l'inventaire et création de mouvements pour chaque détail
            updateInventoryAndCreateMovement(detail, vente);
        }

        // Arrondir le poids total et le convertir en tonnes
        BigDecimal poidsTotalEnTonnes = poidsTotal.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);
        vente.setPoids(poidsTotalEnTonnes.doubleValue());

        // Sauvegarder l'entité de vente avec tous ses détails
        vente = venteRepository.save(vente);
        logger.debug("Entité Vente après la sauvegarde avec ID : {}", vente.getId());
        VenteDto savedVenteDto = venteMapper.toDto(vente);
        logger.info("Fin de la méthode saveVente avec savedVenteDto : {}", savedVenteDto);
        return savedVenteDto;
    }


    @Override
    public VenteDto updateVente(Long venteId, Long nouveauClientId) {
        log.info("Mise à jour du client pour la vente ID: {}", venteId);
        Vente vente = venteRepository.findById(venteId)
                .orElseThrow(() -> {
                    log.error("Vente non trouvée avec l'id: {}", venteId);
                    return new RuntimeException("Impossible ,Vente non trouvée avec l'id: " + venteId);
                });
        // Vérifier si des conditions spécifiques doivent être respectées
        if (vente.getStatut() != StatutVente.EN_COURS) {
            throw new EntityNotFoundException("Cette livraison est déjà terminée donc impossible de changer de destinataire pansez à effectuer un bon de retour.");
        }

        log.info("Vérification de l'existence du nouveau client. ID: {}", nouveauClientId);
        if (!clientRepository.existsById(nouveauClientId)) {
            throw new EntityNotFoundException("Client non trouvé avec l'ID: " + nouveauClientId);
        }

        log.info("Mise à jour du client pour la vente. Nouvel ID Client: {}", nouveauClientId);
        Client nouveauClient = clientRepository.findById(nouveauClientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + nouveauClientId));
        vente.setClient(nouveauClient);

        log.info("Sauvegarde des modifications pour la vente ID: {}", venteId);
        vente = venteRepository.save(vente);

        VenteDto savedVenteDto = venteMapper.toDto(vente);
        log.info("Mise à jour du client terminée pour la vente ID: {}", venteId);
        return savedVenteDto;
    }

    public boolean verifierDisponibiliteArticle(VenteDto venteDto) {
        logger.info("Vérification de la disponibilité du stock pour la vente ID: {}", venteDto.getId());
        for (DetailVenteDto detailVenteDto : venteDto.getDetailVentes()) {
            // Calcul de la quantité totale incluant les bonus
            int quantiteTotale = detailVenteDto.getQuantity() + (detailVenteDto.getBonus() != null ? detailVenteDto.getBonus() : 0);

            Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                            detailVenteDto.getArticleId(), Math.toIntExact(venteDto.getEntrepotId()))
                    .orElse(null);

            // Vérifier si l'inventaire est null ou si la quantité conforme est inférieure à la quantité totale requise
            if (inventaire == null || inventaire.getQuantiteConforme() < quantiteTotale) {
                logger.warn("Stock insuffisant pour l'article ID: {} dans l'entrepôt ID: {}. Requis: {}, Disponible: {}",
                        detailVenteDto.getArticleId(), venteDto.getEntrepotId(), quantiteTotale, inventaire != null ? inventaire.getQuantiteConforme() : "Inventaire inexistant");
                return false;
            }
        }
        logger.info("Stock suffisant pour réaliser la vente ID: {}", venteDto.getId());
        return true;
    }

    private void updateInventoryAndCreateMovement(DetailVente detail, Vente vente) {
        Inventaire inventaire = inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(), vente.getEntrepot().getIdEntre())
                .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: " + detail.getArticle().getIdArticle()));
        // Prendre en compte les bonus
        int totalQuantityChange = -detail.getQuantity(); // Quantité négative pour une vente
        if (detail.getBonus() != null) {
            totalQuantityChange -= detail.getBonus();
        }
        inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + totalQuantityChange);
        inventaireRepository.save(inventaire);
        // Créer un mouvement pour la vente
        Mouvement mouvement = new Mouvement();
        mouvement.setInventaire(inventaire);
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(totalQuantityChange); // Quantité négative pour une sortie
        mouvement.setCondition("CONFORME"); // Ou selon la condition de l'article vendu
        mouvement.setType(TypeMouvement.SORTIE); // Vente est une sortie de stock
        mouvement.setReference(vente.getReference());
        mouvementRepository.save(mouvement);
    }

    @Override
    public VenteDto livrerProduit(Long venteId, LocalDate dateLivraison) {
        log.info("Début du processus de livraison pour la vente ID: {}", venteId);

        Vente vente = venteRepository.findById(venteId)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée avec l'ID: " + venteId));

        if (vente.getStatut() != StatutVente.EN_COURS) {
            log.warn("Tentative de livraison d'une vente qui n'est pas en cours. ID: {}", venteId);
            throw new IllegalStateException("Seules les ventes en cours peuvent être livrées.");
        }

        vente.setStatut(StatutVente.LIVREE);
        vente.setDatelivraison(dateLivraison);

        vente = venteRepository.save(vente);
        log.info("Vente livrée avec succès.");
        return venteMapper.toDto(vente);
    }

    @Override
    public void annulerVente(Long id, String raison) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée avec l'id: " + id));
        // Vérifier que la vente est en cours
        if (vente.getStatut() != StatutVente.EN_COURS) {
            throw new IllegalStateException("On ne peut annuler que les ventes en cours.");
        }
        verifyNotAlreadyCancelled(vente, id);
        vente.setDeleted(true);
        venteRepository.save(vente);
        String refAnnulation = genererRefAnnulation();
        Annulation annulation = createAnnulation(vente, refAnnulation, raison);
        annulationRepository.save(annulation);
        vente.getDetailVentes().forEach(detail -> {
            updateInventory(detail, vente);
            createMovement(detail, vente, TypeMouvement.SORTIE, refAnnulation); // Utilisez refAnnulation ici
        });
        log.info("Vente ID {} a été annulée. Référence d'annulation générée: {}", id, refAnnulation);
    }

    private void createMovement(DetailVente detail, Vente vente, TypeMouvement typeMouvement, String reference) {
        Mouvement mouvement = new Mouvement();
        mouvement.setInventaire(getOrCreateInventory(detail, vente));
        mouvement.setDateMouvement(LocalDateTime.now());
        mouvement.setQuantiteChange(typeMouvement == TypeMouvement.SORTIE ? -detail.getQuantity() : detail.getQuantity());
        mouvement.setCondition(detail.getEtat().name());
        mouvement.setType(typeMouvement);
        mouvement.setReference(reference);
        mouvementRepository.save(mouvement);
    }

    private Inventaire getOrCreateInventory(DetailVente detail, Vente vente) {
        // Logique pour obtenir l'inventaire existant ou en créer un nouveau
        return inventaireRepository.findByArticleIdArticleAndEntrepotIdEntre(
                        detail.getArticle().getIdArticle(),
                        vente.getEntrepot().getIdEntre())
                .orElseGet(() -> new Inventaire(null, vente.getEntrepot(),
                        detail.getArticle(), 0, 0));
    }

    private void updateInventory(DetailVente detail, Vente vente) {
        Inventaire inventaire = getOrCreateInventory(detail, vente);
        int quantityChange = detail.getQuantity();
        adjustInventoryQuantities(inventaire, quantityChange, detail.getEtat());
        inventaireRepository.save(inventaire);
    }

    private void adjustInventoryQuantities(Inventaire inventaire, int quantityChange, Etat etat) {
        // Ajuster les quantités en fonction de l'état de l'article
        if (Etat.CONFORME.equals(etat)) {
            inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + quantityChange);
        } else {
            inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() + quantityChange);
        }
    }


    private void verifyNotAlreadyCancelled(Vente vente, Long id) {
        if (vente.isDeleted()) {
            throw new IllegalStateException("La réception avec l'id: " + id + " a déjà été annulée.");
        }
    }

    private Annulation createAnnulation(Vente receptionStock, String refAnnulation, String raison) {
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
        logger.info("Nombre de ventes pour aujourd'hui (avant incrémentation) : {}", datePart);
        int count = venteRepository.countVenteForToday() + 1;
        return "VD" + datePart + String.format("%04d", count);
    }

    @Override
    public VenteDto getVenteById(Long id) {
        Vente vente = venteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée avec l'id: " + id));
        return venteMapper.toDto(vente);
    }

    @Override
    public void deleteVente(Long id) {
        venteRepository.deleteById(id);
    }

    @Override
    public List<VenteDto> getAllVentes() {
        return venteRepository.findAll().stream()
                .map(venteMapper::toDto)
                .toList();
    }

    @Override
    public Optional<VenteDto> findByReference(String reference) {
        return venteRepository.findByReference(reference)
                .map(venteMapper::toDto);
    }
}
