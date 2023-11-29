package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reception_stock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionStock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;
    private LocalDate dateReception;
    private String quart; // Quart de travail ou session de production

    @OneToMany(mappedBy = "receptionStock", cascade = CascadeType.ALL)
    private List<ReceptionDetail> receptionDetails; // Détails des articles reçus
    @OneToMany(mappedBy = "receptionStock", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mouvement> mouvements; // Les mouvements liés à cette réception
}