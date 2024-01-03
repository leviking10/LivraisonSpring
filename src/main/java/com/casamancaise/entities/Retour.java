package com.casamancaise.entities;

import com.casamancaise.enums.TypeRetour;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "retour")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Retour implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String reference;
    private LocalDate dateRetour;
    @Enumerated(EnumType.STRING)
    @Column(name = "type_retour", nullable = false)
    private TypeRetour typeRetour; // soit un retour sur un transfert vers un client grossiste ou sur une vente
    private String raison; // Raison du retour
    @Column(name = "operation_id", nullable = false)
    private Long operationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @OneToMany(mappedBy = "retour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailRetour> detailsRetours;
}
