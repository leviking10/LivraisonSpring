package com.casamancaise.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcli")
    private Long id;
    @Column(name = "nom")
    private String nom;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "ville")
    private String ville;
    @Column(name = "tel")
    private String telephone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_zone_id", nullable = false)
    private Employee responsableZone;
    @ManyToOne
    @JoinColumn(name = "canal_distrib_id", nullable = false)
    private CanalDistrib canalDistrib;
}