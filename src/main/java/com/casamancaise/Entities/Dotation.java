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
@Table(name = "dotation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dotation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String destinataire;

    @ManyToOne
    private Article article;

    private LocalDate dateDotation;
    private Integer quantite;

    private String motif;
    @OneToMany(mappedBy = "dotation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mouvement> mouvements;
}
