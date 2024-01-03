package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "fonctions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fonction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFonc")
    private Long idFonc;
    @Column(name = "libelle")
    private String libelle;
    @OneToMany(mappedBy = "fonction")
    private Set<Employee> employes;
}