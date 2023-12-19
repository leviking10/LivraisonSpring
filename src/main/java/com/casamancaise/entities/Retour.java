package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "retour")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Retour implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateRetour;
    private String raison; // Raison du retour
    @OneToMany(mappedBy = "retour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailRetour> detailsRetours;

}
