package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehi;

    @Column(name = "matricule", nullable = false, length = 10)
    private String matrVehi;

    @Column(name = "chaufeur", nullable = false, length = 80)
    private String prestataire;

    @Column(name = "telephone", nullable = false, length = 80)
    private String telephone;

    @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transfert> transferts;
}
