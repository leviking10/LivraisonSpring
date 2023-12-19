package com.casamancaise.dto;

import com.casamancaise.entities.Etat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailRetourDto implements Serializable {
    private Long id;
    private Long retourId;
    private Long articleId;
    private Integer quantiteRetournee;
    private Etat etat;

    // Getters, Setters, Constructeurs...
}