package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "mouvement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mouvement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inventaire_id") // Assurez-vous que le nom de la colonne correspond à celui dans votre DB
    private Inventaire inventaire;

    @Column(nullable = false) // Assurez-vous que la date de mouvement ne peut pas être nulle
    private LocalDate dateMouvement;

    private Integer quantiteChange; // La quantité de l'article pour ce mouvement

    private String condition; // La condition de l'article ('conforme' ou 'non conforme')

    @Enumerated(EnumType.STRING) // Spécifier le type d'énumération
    private TypeMouvement type; // Le type de mouvement

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reception_stock_id") // Lier avec la réception de stock
    private ReceptionStock receptionStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfert_id")
    private Transfert transfert;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dotation_id")
    private Dotation dotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vente_id")
    private Vente vente;
}