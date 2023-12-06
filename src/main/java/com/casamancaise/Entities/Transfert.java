package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "transfert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transfert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_entrepot_id", nullable = false)
    private Entrepot fromEntrepot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_entrepot_id", nullable = false)
    private Entrepot toEntrepot;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate; // Utilisation de LocalDate

    @Column(name = "is_received")
    private boolean isReceived;

    @OneToMany(mappedBy = "transfert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferDetails> transferDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehi", nullable = false)
    private Vehicule vehicule;

    @Enumerated(EnumType.STRING)
    private EtatTransfert etat;
    // Relation avec Mouvement
    @OneToMany(mappedBy = "transfert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mouvement> mouvements;

}
