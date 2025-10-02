package com.tcpl.procurement.dto;

import lombok.Data;

@Data
public class PurchaseRequisitionRequest {
    private Long projectId;
    private String materialName;
    private Integer quantity;
    private String unit;
    private Long reqBy; // 'String requestedBy' ko 'Long reqBy' se replace karein
    private String remarks;
    private java.math.BigDecimal totalAmount;
}