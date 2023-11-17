package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detail_commande")
public class DetailCommande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetailCommande;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private Double prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande; // Ceci est référencé par 'mappedBy' dans Commande

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article; // Ceci est référencé par 'mappedBy' dans Article


}