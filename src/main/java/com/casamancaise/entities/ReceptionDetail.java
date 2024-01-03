package com.casamancaise.entities;

import com.casamancaise.enums.Etat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "reception_detail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceptionDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reception_stock_id", nullable = false)
    private ReceptionStock receptionStock;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
    @NotNull
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Etat etat = Etat.CONFORME;

    @Override
    public String toString() {
        return "ReceptionDetail{" +
                "id=" + id +
                ", receptionStock=" + receptionStock +
                ", article=" + article +
                ", quantity=" + quantity +
                ", etat=" + etat +
                '}';
    }
}