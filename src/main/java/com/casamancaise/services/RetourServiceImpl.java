package com.casamancaise.services;

import com.casamancaise.dao.*;
import com.casamancaise.dto.DetailRetourDto;
import com.casamancaise.dto.RetourDto;
import com.casamancaise.entities.*;
import com.casamancaise.enums.Etat;
import com.casamancaise.enums.TypeDestinataire;
import com.casamancaise.enums.TypeMouvement;
import com.casamancaise.enums.TypeRetour;
import com.casamancaise.mapping.DetailRetourMapper;
import com.casamancaise.mapping.RetourMapper;
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

    public RetourServiceImpl(RetourRepository retourRepository, RetourMapper retourMapper, TransfertRepository transfertRepository, VenteRepository venteRepository, DetailRetourMapper detailRetourMapper, MouvementRepository mouvementRepository, InventaireRepository inventaireRepository) {
        this.retourRepository = retourRepository;
        this.retourMapper = retourMapper;
        this.transfertRepository = transfertRepository;
        this.venteRepository = venteRepository;
        this.detailRetourMapper = detailRetourMapper;
        this.mouvementRepository = mouvementRepository;
        this.inventaireRepository = inventaireRepository;
    }

    @Override
    public RetourDto saveRetour(RetourDto retourDto) {
        log.info("Début de la méthode saveRetour avec retourDto: {}", retourDto);
        LocalDate today = LocalDate.now();

        // Convertir DTO en entité
        Retour retour = retourMapper.toEntity(retourDto);
        retour.setDateRetour(today);
        retour.setReference(generateReference());
        // Traitement selon le type de retour
        if (retourDto.getTypeRetour() == TypeRetour.VENTE) {
            traiterRetourVente(retourDto);
        } else if (retourDto.getTypeRetour() == TypeRetour.TRANSFERT) {
            traiterRetourTransfert(retourDto);
        }

        // Traitement des détails de retour
        traiterDetailsRetour(retour, retourDto.getDetailsRetours());
        log.debug("Traitement des détails de retour effectué");

        // Mise à jour de l'inventaire
        majInventaire(retourDto);
        log.debug("Mise à jour de l'inventaire effectuée");

        // Création des mouvements
        creerMouvements(retourDto);
        log.debug("Création des mouvements effectuée");

        // Sauvegarde de l'entité de retour
        retour = retourRepository.save(retour);
        log.debug("Entité Retour sauvegardée avec ID : {}", retour.getId());

        if (retour.getId() == null) {
            log.error("L'ID de Retour est null après la sauvegarde.");
            throw new IllegalStateException("L'ID de Retour ne peut pas être null après la sauvegarde.");
        }

        RetourDto savedRetourDto = retourMapper.toDto(retour);
        log.info("Fin de la méthode saveRetour avec savedRetourDto : {}", savedRetourDto);
        return savedRetourDto;
    }

    private void traiterRetourTransfert(RetourDto retourDto) throws RuntimeException {
        // Rechercher le transfert par son ID
        Transfert transfert = transfertRepository.findById(retourDto.getOperationId())
                .orElseThrow(() -> new RuntimeException("Transfert non trouvé pour l'ID: " + retourDto.getOperationId()));
        // Vérifier si le type de destinataire est 'CLIENT'
        if (transfert.getTypeDestinataire() != TypeDestinataire.CLIENT)
            throw new RuntimeException("Le transfert avec l'ID: " + retourDto.getOperationId() + " n'est pas destiné à un client");
        log.info("Traitement du retour pour le transfert destiné au client, ID du transfert: {}", transfert.getId());

    }

    private void traiterRetourVente(RetourDto retourDto) {
        Vente vente = venteRepository.findById(retourDto.getOperationId())
                .orElseThrow(() -> new RuntimeException("Vente non trouvée pour l'ID: " + retourDto.getOperationId()));
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

    //Mettons à jour l'etat du stock
    private void majInventaire(RetourDto retourDto) {
        for (DetailRetourDto detailDto : retourDto.getDetailsRetours()) {
            Inventaire inventaire = inventaireRepository
                    .findByArticleIdArticleAndEntrepotIdEntre(detailDto.getArticleId(), Math.toIntExact(retourDto.getEntrepotId()))
                    .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: "
                            + detailDto.getArticleId() + " et entrepôt: " + retourDto.getEntrepotId()));
            int quantityChange = detailDto.getQuantiteRetournee();
            // Ajuster l'inventaire en fonction de l'état du retour
            if (detailDto.getEtat() == Etat.CONFORME) {
                inventaire.setQuantiteConforme(inventaire.getQuantiteConforme() + quantityChange);
            } else {
                inventaire.setQuantiteNonConforme(inventaire.getQuantiteNonConforme() + quantityChange);
            }
            inventaireRepository.save(inventaire);
        }
    }

    //Gerons la traçabilité en faisant aussi l'enregistrement sur la table mouvement
    private void creerMouvements(RetourDto retourDto) {
        for (DetailRetourDto detailDto : retourDto.getDetailsRetours()) {
            Inventaire inventaire = inventaireRepository
                    .findByArticleIdArticleAndEntrepotIdEntre(detailDto.getArticleId(), Math.toIntExact(retourDto.getEntrepotId()))
                    .orElseThrow(() -> new RuntimeException("Inventaire non trouvé pour l'article: "
                            + detailDto.getArticleId() + " et entrepôt: " + retourDto.getEntrepotId()));

            Mouvement mouvement = new Mouvement();
            mouvement.setInventaire(inventaire);
            mouvement.setDateMouvement(LocalDateTime.now());
            mouvement.setQuantiteChange(detailDto.getQuantiteRetournee());
            mouvement.setCondition(detailDto.getEtat().name());
            mouvement.setType(TypeMouvement.ENTREE);
            mouvement.setReference(retourDto.getReference());
            mouvementRepository.save(mouvement);
        }
    }

    @Override
    public RetourDto getRetourById(Long id) {
        Retour retour = retourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Retour not found"));
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

    @Override
    public Optional<RetourDto> findByReference(String reference) {
        return retourRepository.findByReference(reference)
                .map(retourMapper::toDto);
    }
}
