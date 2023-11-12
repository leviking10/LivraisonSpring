package com.casamancaise.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article")
public class Article {

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
    // Liste des détails de commandes pour cet article
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DetailCommande> detailsCommandes;

    // Liste des détails de stock pour cet article
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DetailsStock> detailsStocks;

}