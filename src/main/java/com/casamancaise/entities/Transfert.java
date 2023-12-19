package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.*;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "type_destinataire", nullable = false)
    private TypeDestinataire typeDestinataire;
    @Column(name = "destinataire_id", nullable = false)
    private Integer destinataireId;
    @Column(unique = true, nullable = false)
    private String reference;
    private LocalDate transferDate;
    @Column(name = "date_reception")
    private LocalDate ReceptionDate;
    @OneToMany(mappedBy = "transfert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferDetails> transferDetails;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @Enumerated(EnumType.STRING)
    private EtatTransfert etat;

    @Override
    public String toString() {
        return "Transfert{" +
                "id=" + id +
                ", fromEntrepot=" + fromEntrepot +
                ", typeDestinataire=" + typeDestinataire +
                ", destinataireId=" + destinataireId +
                ", reference='" + reference + '\'' +
                ", transferDate=" + transferDate +
                ", LivraisonDate=" + ReceptionDate +
                ", transferDetails=" + transferDetails +
                ", vehicule=" + vehicule +
                ", etat=" + etat +
                '}';
    }
}
