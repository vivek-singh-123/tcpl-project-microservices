package com.tcpl.billing.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice_items")
@Data // => Getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private double quantity;
    private double rate;
    private double amount;
    private String hsnCode;
    private double gstPercent;

    // Many InvoiceItems belong to one Invoice
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    private com.tcpl.billing.model.Invoice invoice;

}
