package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
@Entity
@Table(name = "detail_vente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailVente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vente_id")
    private Vente vente; // La vente associée à ce détail

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article; // Le produit vendu
    private Integer quantity; // La quantité vendue
    private Integer bonus;
    private BigDecimal poids;

}
