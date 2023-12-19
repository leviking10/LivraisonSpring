package com.casamancaise.entities;
import jakarta.persistence.*;

import lombok.*;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "entrepots")
@Getter
@Setter
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

        public Entrepot(Integer idEntre) {
                this.idEntre = idEntre;
        }
}