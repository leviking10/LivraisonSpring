package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entrepot_id", nullable = false)
    private Entrepot entrepot;
    private LocalDate dateReception;
    private String quart; // Quart de travail ou session de production
    @Column(unique = true, nullable = false)
    private String reference;
    private boolean isDeleted;
    @OneToMany(mappedBy = "receptionStock", cascade = CascadeType.ALL)
    private List<ReceptionDetail> receptionDetails;// Détails des articles reçus
}