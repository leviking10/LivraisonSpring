package com.casamancaise.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "canal_distrib")
public class CanalDistrib  {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer idCanal;
        @Column(name = "canal", nullable = false, length = 15)
        private String canal;
        @OneToMany(mappedBy = "canalDistrib")
        private List<Client> clients;
    }
