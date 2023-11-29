package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "localite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Localite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private BigDecimal prix;
}
