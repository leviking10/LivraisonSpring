package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "entrepots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrepot implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idEntre;

        @Column(name = "nom", nullable = false, length = 80)
        private String nomEntr;
        @Column(name = "localite", nullable = false, length = 80)
        private String localEntr;
        @OneToMany(mappedBy = "fromEntrepot", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Transfert> transfertsFrom;

        @OneToMany(mappedBy = "toEntrepot", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Transfert> transfertsTo;

        @OneToMany(mappedBy = "entrepot", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<StockUsine> stockUsines;
}