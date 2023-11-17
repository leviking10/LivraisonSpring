package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "canal_distrib")
public class CanalDistrib  implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idCanal;
        @Column(name = "canal", nullable = false, length = 100)
        private String canal;
        @OneToMany(mappedBy = "canalDistrib")
        private Set<Client> clients;
    }
