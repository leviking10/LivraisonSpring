package com.casamancaise.Entities;
import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table(name = "audit_trails")
public class AuditTrail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "action_details", columnDefinition = "TEXT")
    private String actionDetails;

    @Column(name = "performed_by", nullable = false)
    private Long performedById;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "performed_at", nullable = false)
    private Date performedAt;

    // Constructors
    public AuditTrail() {
    }

    public AuditTrail(String entityType, Long entityId, String action, String actionDetails, Long performedById, Date performedAt) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.actionDetails = actionDetails;
        this.performedById = performedById;
        this.performedAt = performedAt;
    }

    // Getters and setters
    // ... (omitted for brevity)

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "AuditTrail{" +
                "id=" + id +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", action='" + action + '\'' +
                ", actionDetails='" + actionDetails + '\'' +
                ", performedById=" + performedById +
                ", performedAt=" + performedAt +
                '}';
    }
}
