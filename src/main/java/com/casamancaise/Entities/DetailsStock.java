package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "details_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailsStock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article article;

    @Column(name = "quantite", nullable = false)
    private int quantite;

    @Column(name = "stock_usine_id", nullable = false)
    private Long stockUsineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_usine_id", insertable = false, updatable = false)
    private StockUsine stockUsine;
}