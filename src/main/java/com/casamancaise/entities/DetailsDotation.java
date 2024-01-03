package com.casamancaise.entities;

import com.casamancaise.enums.Etat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "detail_dotations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsDotation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dotation_id")
    private Dotation dotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private Etat etat;
}
