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
@Table(name = "dotations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dotation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrepot_id")
    private Entrepot entrepot;
    private String destinataire;
    private LocalDate dateDotation;
    private String motif;
    private boolean isDeleted;
    @OneToMany(mappedBy = "dotation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailsDotation> detailsDotation;
}
