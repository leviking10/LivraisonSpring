package com.casamancaise.entities;

import com.casamancaise.enums.Etat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
    @Enumerated(EnumType.STRING)
    @NotNull
    private Etat etat = Etat.CONFORME;
}
