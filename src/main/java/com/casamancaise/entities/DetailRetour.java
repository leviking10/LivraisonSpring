package com.casamancaise.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
@Entity
@Table(name = "detail_retour")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailRetour implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "retour_id")
    private Retour retour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    private Integer quantiteRetournee;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Etat etat;

}