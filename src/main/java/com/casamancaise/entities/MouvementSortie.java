package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "mouvement_sortie")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MouvementSortie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;
    @Column(nullable = false)
    private LocalDate dateSortie;
    @Column(unique = true, nullable = false)
    private String reference;
    @Column(nullable = true)
    private String motif;
    @OneToMany(mappedBy = "mouvementSortie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MouvementSortieDetail> detailsSortie;

    @Override
    public String toString() {
        return "MouvementSortie{" +
                "id=" + id +
                ", entrepot=" + entrepot +
                ", dateMouvementSortie=" + dateSortie+
                ", reference='" + reference + '\'' +
                ", motif='" + motif + '\'' +
                ", detailsSortie=" + detailsSortie +
                '}';
    }
}