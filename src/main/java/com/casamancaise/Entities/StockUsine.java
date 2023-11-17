package com.casamancaise.entities;
import jakarta.persistence.*;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "stock_usine")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockUsine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entrepot_id", nullable = false)
    private int entrepotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id", insertable = false, updatable = false)
    private Entrepot entrepot;

    @Column(name = "quart", length = 50)
    private String quart;

    @Column(name = "date_entree", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateEntree;

    @OneToMany(mappedBy = "stockUsine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DetailsStock> detailsStocks;
}
