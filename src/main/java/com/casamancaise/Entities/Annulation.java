package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
@Entity
@Table(name = "annulation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Annulation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String ref;
    @Column(name = "ref_reception", nullable = false)
    private String refReception;
    @Column(name = "date_annulation", nullable = false)
    private LocalDate dateAnnulation;
    @Column(nullable = false)
    private String raison;
}
