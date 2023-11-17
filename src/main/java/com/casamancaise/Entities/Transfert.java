package com.casamancaise.entities;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transfert")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_entrepot_id", nullable = false)
    private Integer fromEntrepotId;

    @Column(name = "to_entrepot_id", nullable = false)
    private Integer toEntrepotId;

    @Column(name = "transfer_date", nullable = false)
    @Temporal(TemporalType.DATE) // This annotation is for date only without time
    private Date transferDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_entrepot_id", insertable = false, updatable = false)
    private Entrepot fromEntrepot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_entrepot_id", insertable = false, updatable = false)
    private Entrepot toEntrepot;

    @Column(name = "is_received")
    private boolean isReceived;

    @OneToMany(mappedBy = "transfert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferDetails> transferDetails;

    @Column(name = "id_vehi", nullable = false)
    private Long idVehi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehi", insertable = false, updatable = false)
    private Vehicule vehicule;
}
