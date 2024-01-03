package com.casamancaise.entities;

import com.casamancaise.enums.TypeMouvement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mouvement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventaire_id")
    private Inventaire inventaire;
    @Column(nullable = false) // Assurez que la date de mouvement ne peut pas être nulle
    private LocalDateTime dateMouvement;
    private Integer quantiteChange; // La quantité de l'article pour ce mouvement
    private String condition; // La condition de l'article ('conforme' ou 'non conforme')
    @Enumerated(EnumType.STRING) // Spécifier le type d'énumération
    private TypeMouvement type; // Le type de mouvement
    @Column(nullable = false)
    private String reference;
}