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
@Table(name = "mouvement_sortie_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MouvementSortieDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
    @Column(nullable = false)
    private Integer quantite;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Etat etat = Etat.NON_CONFORME;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mouvement_sortie_id", nullable = false)
    private MouvementSortie mouvementSortie;

    @Override
    public String toString() {
        return "MouvementSortieDetail{" +
                "id=" + id +
                ", article=" + article +
                ", quantite=" + quantite +
                ", etat=" + etat +
                ", mouvementSortie=" + mouvementSortie +
                '}';
    }
}