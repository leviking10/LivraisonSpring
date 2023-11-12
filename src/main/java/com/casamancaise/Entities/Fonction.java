package com.casamancaise.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "fonctions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fonction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFonc")
    private Long idFonc;
    @Column(name = "libelle")
    private String libelle;
    @OneToMany(mappedBy = "fonction", fetch = FetchType.LAZY)
    private List<Employee> employes;
}