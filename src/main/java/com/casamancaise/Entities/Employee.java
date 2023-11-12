package com.casamancaise.Entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_complet")
    private String nomComplet;

    @Column(name = "localite")
    private String localite;

    @Column(name = "tel")
    private String telephone;

    @Column(name = "fonction_id")
    private Long fonctionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fonction_id", referencedColumnName = "idFonc", insertable = false, updatable = false)
    private Fonction fonction;
}