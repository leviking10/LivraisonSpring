package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "employes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_complet")
    private String nomComplet;

    @Column(name = "localite")
    private String localite;

    @Column(name = "tel")
    private String telephone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true, name = "fonction_id")
    private Fonction fonction;
    @OneToMany(mappedBy = "responsableZone", fetch = FetchType.LAZY)
    private Set<Client> clients;
}