package com.tcpl.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private Long materialId;

    @Column(precision = 12, scale = 2) // For BigDecimal
    private BigDecimal quantity;

    private String location;

    public Inventory(Long id, Long projectId, Long materialId, BigDecimal quantity, String location) {
        this.id = id;
        this.projectId = projectId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.location = location;
    }
}