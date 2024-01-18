package com.casamancaise.entities;

import com.casamancaise.enums.StatutVente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ventes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String reference;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailVente> detailVentes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id")
    private Entrepot entrepot;
    private LocalDate dateVente;
    private boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private StatutVente statut = StatutVente.EN_COURS;

    private LocalDate datelivraison;
}
