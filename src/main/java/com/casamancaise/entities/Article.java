package com.casamancaise.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArticle;
    @Column(name = "ref_arti", length = 20, nullable = false)
    private String refArti;

    @Column(name = "lib_arti", length = 50, nullable = false)
    private String libArti;

    @Column(name = "cout_prod", length = 20, nullable = false)
    private Double coutProd;

    @Column(name = "unite", length = 20, nullable = false)
    private String unite;

    @Column(name = "marge_secu", length = 20, nullable = false)
    private Double margeSecu;

    @Column(name = "tonage", length = 20, nullable = false)
    private Double tonage;
    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ReceptionDetail> receptionDetails;

    public Article(Long idArticle) {
        this.idArticle = idArticle;
    }

}