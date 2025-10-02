package com.tcpl.procurement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pr_id")
    private Long prId;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "po_no")
    private String poNo;

    private BigDecimal amount;

    private String status; // draft, sent, accepted, rejected, delivered, closed

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "pr_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PurchaseRequisition requisition;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "draft";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
