package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "transfer_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_id", nullable = false)
    private Long articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    private Article article;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "transfert_id", nullable = false)
    private Long transfertId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfert_id", insertable = false, updatable = false)
    private Transfert transfert;
}
