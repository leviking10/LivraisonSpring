package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "canal_distrib")
public class CanalDistrib implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCanal;
    @Column(name = "canal", nullable = false, length = 100)
    private String canal;
    @OneToMany(mappedBy = "canalDistrib")
    private Set<Client> clients;
}
