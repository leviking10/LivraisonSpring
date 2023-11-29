package com.casamancaise.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "reception_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reception_stock_id", nullable = false)
    private ReceptionStock receptionStock;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
    @NotNull
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Etat etat;
}